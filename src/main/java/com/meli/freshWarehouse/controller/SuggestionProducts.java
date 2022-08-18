package com.meli.freshWarehouse.controller;

import com.meli.freshWarehouse.dto.ProductSuggestionResponseDto;
import com.meli.freshWarehouse.service.SuggestionProductsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/suggestions")
public class SuggestionProducts {

    @Autowired
    private SuggestionProductsService service;

    @GetMapping
    public ResponseEntity<List<ProductSuggestionResponseDto>> getProductInCartByUser() {
        return new ResponseEntity<>(service.getAllProductInCart(), HttpStatus.OK);
    }


}
