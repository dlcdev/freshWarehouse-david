package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.*;
import com.meli.freshWarehouse.model.PurchaseOrder;
import com.meli.freshWarehouse.model.ShoppingCartProduct;
import com.meli.freshWarehouse.repository.BatchRepo;
import com.meli.freshWarehouse.repository.PurchaseRepo;
import com.meli.freshWarehouse.repository.ShoppingCartProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.meli.freshWarehouse.service.PurchaseOrderService.getTotalPrice;

@Service
public class SuggestionProductsService implements ISuggestionProducts {

    private final PurchaseRepo purchaseRepo;

    private final PurchaseOrderService purchaseOrderService;
    private final ProductService productService;
    private final ShoppingCartProductRepo shoppingCartProductRepo;

    private final BuyerService buyerService;

    private final BatchRepo batchRepo;


    public SuggestionProductsService(PurchaseRepo purchaseRepo, PurchaseOrderService purchaseOrderService, ProductService productService, ShoppingCartProductRepo shoppingCartProductRepo, BuyerService buyerService, BatchRepo batchRepo) {
        this.purchaseRepo = purchaseRepo;
        this.purchaseOrderService = purchaseOrderService;
        this.productService = productService;
        this.shoppingCartProductRepo = shoppingCartProductRepo;
        this.buyerService = buyerService;
        this.batchRepo = batchRepo;
    }

    @Override
    public List<ProductSuggestionResponseDto> getAllProductInCart() {

        List<PurchaseOrder> purchaseOrder = purchaseRepo.findAllByOrderStatusEndingWith("Open");
        List<ProductSuggestionResponseDto> productSuggestionResponseDtoList = new ArrayList<>();

        return getProductSuggestion(purchaseOrder, productSuggestionResponseDtoList);
    }

    @Override
    public PurchaseOrderResponseDTO save(ProductSDto productSDto) {

        PurchaseOrder getPurchaseOrderInDataBaseList = purchaseOrderService.getById(productSDto.getOrderId());

        productSDto.getNewProducts().forEach(p -> {
            getPurchaseOrderInDataBaseList.getShoppingCartProducts().add(
                    ShoppingCartProduct
                            .builder()
                            .product(productService.getProductById(p.getProductId()))
                            .quantity(p.getQuantity())
                            .build()
            );
        });

        for (ShoppingCartProduct shopping : getPurchaseOrderInDataBaseList.getShoppingCartProducts()) {
            System.out.println(shopping.getId());
            if (shopping.getId() == null) {
                shopping.setPurchaseOrder(getPurchaseOrderInDataBaseList);
            }
        }

        shoppingCartProductRepo.saveAll(getPurchaseOrderInDataBaseList.getShoppingCartProducts());

        List<ProductResponseDto> productSavedInCart = new ArrayList<>();

        List<ProductSuggestionDto> productSuggestion = purchaseOrderService.getSuggestionsProduct(
                getPurchaseOrderInDataBaseList.getShoppingCartProducts(), productSavedInCart
        );

        return PurchaseOrderResponseDTO.builder()
                .orderId(getPurchaseOrderInDataBaseList.getId())
                .productsInCart(productSavedInCart)
                .statusOrder(getPurchaseOrderInDataBaseList.getOrderStatus())
                .suggestionProduct(productSuggestion)
                .totalPrice(getTotalPrice(getPurchaseOrderInDataBaseList.getShoppingCartProducts()))
                .build();
    }

    private List<ProductSuggestionResponseDto> getProductSuggestion(
            List<PurchaseOrder> purchaseOrder, List<ProductSuggestionResponseDto> productSuggestionResponseDtoList) {
        for (PurchaseOrder purchase : purchaseOrder) {
            productSuggestionResponseDtoList.add(
                    ProductSuggestionResponseDto
                            .builder()
                            .orderId(purchase.getId())
                            .statusOrder(purchase.getOrderStatus())
                            .totalPrice(getTotalPrice(purchase.getShoppingCartProducts()))
                            .productsInCart(getProductInCart(purchase.getShoppingCartProducts()))
                            .build()
            );

        }
        return productSuggestionResponseDtoList;
    }

    private List<ProductResponseDto> getProductInCart(Set<ShoppingCartProduct> shoppingCartProducts) {
        List<ProductResponseDto> productResponseList = new ArrayList<>();

        for (ShoppingCartProduct cart : shoppingCartProducts) {
            productResponseList.add(ProductResponseDto.builder()
                    .productName(cart.getProduct().getName())
                    .quantity(cart.getQuantity())
                    .build());
        }

        return productResponseList;

    }
}
