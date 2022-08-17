package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.ProductResponseDto;

public interface ISuggestionProducts {

    ProductResponseDto getProductByUser(Long id);


}
