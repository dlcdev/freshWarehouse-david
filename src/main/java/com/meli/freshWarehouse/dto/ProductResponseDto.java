package com.meli.freshWarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseDto {
    private String productName;
    private Integer quantity;

}
