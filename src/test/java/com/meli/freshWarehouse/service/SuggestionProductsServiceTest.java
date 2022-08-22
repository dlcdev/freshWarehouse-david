package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.ProductSDto;
import com.meli.freshWarehouse.dto.PurchaseOrderResponseDTO;
import com.meli.freshWarehouse.util.GenerateSuggestionProduct;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SuggestionProductsServiceTest {

    @Mock
    private SuggestionProductsService productsService;


    @Test
    void getAllProductInCart() {

//        BDDMockito.when(ArgumentMatchers.any(PurchaseOrderResponseDTO.class))
//                .thenReturn()

        ProductSDto productSDto = GenerateSuggestionProduct.newProductSuggestion();
        PurchaseOrderResponseDTO responseDTO = productsService.save(productSDto);

        assertThat(responseDTO);


    }

    @Test
    void save() {
    }

}