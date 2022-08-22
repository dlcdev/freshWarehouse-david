package com.meli.freshWarehouse.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductSDto {
    private Long orderId;
    private List<ProductResponseSDto> newProducts;
}
