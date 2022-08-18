package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.ProductResponseDto;
import com.meli.freshWarehouse.dto.ProductSuggestionResponseDto;
import com.meli.freshWarehouse.model.PurchaseOrder;
import com.meli.freshWarehouse.model.ShoppingCartProduct;
import com.meli.freshWarehouse.repository.PurchaseRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.meli.freshWarehouse.service.PurchaseOrderService.getTotalPrice;

@Service
public class SuggestionProductsService implements ISuggestionProducts {

    private final PurchaseRepo purchaseRepo;

    public SuggestionProductsService(PurchaseRepo purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    @Override
    public List<ProductSuggestionResponseDto> getAllProductInCart() {

        List<PurchaseOrder> purchaseOrder = purchaseRepo.findAllByOrderStatusEndingWith("Open");
        List<ProductSuggestionResponseDto> productSuggestionResponseDtoList = new ArrayList<>();

        return getProductSuggestion(purchaseOrder, productSuggestionResponseDtoList);
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
