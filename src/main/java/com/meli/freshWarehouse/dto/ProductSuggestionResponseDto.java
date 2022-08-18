package com.meli.freshWarehouse.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductSuggestionResponseDto {

    private Long orderId;
    private String statusOrder;
    private Double totalPrice;
    private List<ProductResponseDto> productsInCart;

}
