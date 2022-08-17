package com.meli.freshWarehouse.controller;

import com.meli.freshWarehouse.dto.ProductSuggestionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/suggestions")
public class SuggestionProducts {

    @Autowired
    private SuggestionProducts service;

    @GetMapping
    public ResponseEntity<List<ProductSuggestionResponseDto>> getProductInCartByUser(Long id) {
        return service.getProductInCartByUser(id);
    }



}
