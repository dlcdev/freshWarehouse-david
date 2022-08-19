package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.*;
import com.meli.freshWarehouse.exception.ExceededStock;
import com.meli.freshWarehouse.exception.NotFoundException;
import com.meli.freshWarehouse.model.*;
import com.meli.freshWarehouse.repository.BatchRepo;
import com.meli.freshWarehouse.repository.PurchaseRepo;
import com.meli.freshWarehouse.repository.ShoppingCartProductRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    private final BuyerService buyerService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final PurchaseRepo purchaseRepo;
    private final ShoppingCartProductRepo shoppingCartProductRepo;
    private final BatchRepo batchRepo;

    public PurchaseOrderService(BuyerService buyerService, WarehouseService warehouseService, ProductService productService, PurchaseRepo purchaseRepo, ShoppingCartProductRepo shoppingCartProductRepo, BatchRepo batchRepo) {
        this.buyerService = buyerService;
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.purchaseRepo = purchaseRepo;
        this.shoppingCartProductRepo = shoppingCartProductRepo;
        this.batchRepo = batchRepo;
    }

    @Override
    public PurchaseOrderResponseDTO save(PurchaseOrderDto purchaseOrderDto) {
        Set<ShoppingCartProduct> shoppingCartProducts = new HashSet<>();
        purchaseOrderDto.getShoppingCartProducts().forEach(p -> {
            shoppingCartProducts.add(ShoppingCartProduct.builder()
                    .product(productService.getProductById(p.getProductId()))
                    .quantity(p.getQuantity())
                    .build());
        });


        PurchaseOrder purchaseOrder = purchaseRepo.save(PurchaseOrder.builder()
                .buyer(buyerService.getBuyerById(purchaseOrderDto.getBuyerId()))
                .orderStatus("OPEN")
                .date(LocalDate.parse(purchaseOrderDto.getDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build());

        shoppingCartProducts.forEach(p -> {
            p.setPurchaseOrder(purchaseOrder);
        });

        purchaseOrder.setShoppingCartProducts(shoppingCartProducts);

        shoppingCartProductRepo.saveAll(shoppingCartProducts);

        Set<ShoppingCartProduct> getPurchaseOrderInDataBaseList = getById(
                purchaseOrder.getId()).getShoppingCartProducts();

        List<ProductResponseDto> productSavedInCart = new ArrayList<>();
        List<ProductSuggestionDto> productSuggestion = new ArrayList<>();

        getSuggestionsProduct(getPurchaseOrderInDataBaseList, productSavedInCart, productSuggestion);

        return PurchaseOrderResponseDTO.builder()
                .orderId(purchaseOrder.getId())
                .productsInCart(productSavedInCart)
                .statusOrder(purchaseOrder.getOrderStatus())
                .suggestionProduct(productSuggestion)
                .totalPrice(getTotalPrice(purchaseOrder.getShoppingCartProducts()))
                .build();
    }

    public static Double getTotalPrice(Set<ShoppingCartProduct> shoppingCartProducts) {
        return shoppingCartProducts.stream()
                .reduce(0D,
                        (partialTotalPrice, shoppingCartProduct) ->
                                partialTotalPrice +
                                        (shoppingCartProduct.getProduct().getPrice()
                                                * shoppingCartProduct.getQuantity()), Double::sum);
    }

    private void getSuggestionsProduct(Set<ShoppingCartProduct> getPurchaseOrderInDataBaseList, List<ProductResponseDto> productSavedInCart, List<ProductSuggestionDto> productSuggestion) {
        for (ShoppingCartProduct productList : getPurchaseOrderInDataBaseList) {
            productSavedInCart.add(ProductResponseDto.builder()
                    .productName(productList.getProduct().getName())
                    .quantity(productList.getQuantity())
                    .build());

            List<String> sectionsNames = productList.getProduct().getSections().stream().map(s -> s.getName())
                    .collect(Collectors.toList());

            for (String sectionName : sectionsNames) {
                productSuggestion.addAll(batchRepo.getStockBySectionName(sectionName));
            }

        }
    }

    @Override
    public PurchaseOrder getById(Long purchaseOrderId) {
        return purchaseRepo.findById(purchaseOrderId).orElseThrow(
                () -> new NotFoundException("Can't find purchase order with the informed id"));
    }

    @Override
    public PurchaseOrderTotalPriceDTO finalizePurchaseOrder(Long purchaseOrderId, PurchaseOrderDto purchaseOrderDto) {
        PurchaseOrder purchaseOrder = this.getById(purchaseOrderId);
        if (purchaseOrder.getOrderStatus().equals("CLOSE")) {
            throw new NotFoundException("Not permitted");
        }

        validStockQuantityAndDueDate(purchaseOrder);

        purchaseOrder.setOrderStatus("CLOSE");

        purchaseRepo.save(purchaseOrder);

        batchRepo.saveAll(updateStockAfterPurchaseOrder(purchaseOrder));

        return PurchaseOrderTotalPriceDTO.builder()
                .id(purchaseOrder.getId())
                .totalPrice(purchaseOrder.getShoppingCartProducts().stream()
                        .reduce(0D,
                                (partialTotalPrice, shoppingCartProduct) ->
                                        partialTotalPrice + shoppingCartProduct.getProduct().getPrice(), Double::sum)
                )
                .build();
    }

    private void validStockQuantityAndDueDate(PurchaseOrder purchaseOrder) {
        purchaseOrder.getShoppingCartProducts().stream().forEach(p -> {
            Long totalOfQuantityInStock = warehouseService.getStockOfProductById(p.getProduct().getId()).getWarehouses().stream()
                    .reduce(0L,
                            (partialQuantity, warehouse) -> partialQuantity + warehouse.getTotalQuantity(), Long::sum);
            if (totalOfQuantityInStock < p.getQuantity()) {
                throw new ExceededStock("Quantity of " + p.getProduct().getName() + " in purchase order exceeds current stock value. Total quantity in stock: "
                        + totalOfQuantityInStock);
            }
            int totalOfQuantityInStockWithDueDateAvailable = 0;
            for (Batch b : p.getProduct().getListBatch()) {
                if (b.getDueDate().isAfter(LocalDate.now().plusWeeks(3))) {
                    totalOfQuantityInStockWithDueDateAvailable += b.getCurrentQuantity();
                }
            }
            if (totalOfQuantityInStockWithDueDateAvailable < p.getQuantity()) {
                throw new NotFoundException("Can't add " + p.getProduct().getName() + " to shopping cart. " + p.getProduct().getName() + " expiring date already expirated.");
            }
        });
    }

    private List<Batch> updateStockAfterPurchaseOrder(PurchaseOrder purchaseOrder) {
        List<Batch> batchList = new ArrayList<>();
        purchaseOrder.getShoppingCartProducts().stream().forEach(p -> {
            Integer quantity = p.getQuantity();
            for (Batch b : p.getProduct().getListBatch()) {
                if (b.getDueDate().isAfter(LocalDate.now().plusWeeks(3))) {
                    if (quantity == 0) {
                        break;
                    } else if (quantity > b.getCurrentQuantity()) {
                        quantity = quantity - b.getCurrentQuantity();
                        b.setCurrentQuantity(0);
                    } else if (quantity < b.getCurrentQuantity()) {
                        b.setCurrentQuantity(b.getCurrentQuantity() - quantity);
                        quantity = 0;
                    } else {
                        b.setCurrentQuantity(0);
                        quantity = 0;
                    }
                    batchList.add(b);
                }
            }
        });
        return batchList;
    }
}
