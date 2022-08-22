package com.meli.freshWarehouse.util;

import com.meli.freshWarehouse.dto.ProductResponseSDto;
import com.meli.freshWarehouse.dto.ProductSDto;
import com.meli.freshWarehouse.dto.SellerDto;

import java.util.Arrays;
import java.util.List;

public class GenerateSuggestionProduct {

    public static final ProductSDto newProductSuggestion() {

        List<ProductResponseSDto> productResponseSDtoList = Arrays.asList(
                ProductResponseSDto.builder()
                        .productId(1L)
                        .quantity(1)
                        .build(),
                ProductResponseSDto.builder()
                        .productId(2L)
                        .quantity(2)
                        .build()
        );

        return ProductSDto.builder()
                .orderId(1L)
                .newProducts(productResponseSDtoList)
                .build();
    }

}
