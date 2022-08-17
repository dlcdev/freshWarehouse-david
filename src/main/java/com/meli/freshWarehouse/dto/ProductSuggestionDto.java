package com.meli.freshWarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductSuggestionDto {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;

}
