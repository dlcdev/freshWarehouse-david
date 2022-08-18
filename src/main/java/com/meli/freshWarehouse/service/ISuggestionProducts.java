package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.ProductSuggestionResponseDto;
import com.meli.freshWarehouse.model.PurchaseOrder;

import java.util.List;

public interface ISuggestionProducts {

    List<ProductSuggestionResponseDto> getAllProductInCart();


}
