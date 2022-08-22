package com.meli.freshWarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseSDto {
    private Long productId;
    private Integer quantity;

}
