package com.meli.freshWarehouse.controller;

import com.meli.freshWarehouse.dto.PurchaseOrderResponseDTO;
import com.meli.freshWarehouse.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/v1/fresh-products/suggestions")
public class SuggestionProducts {

    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDTO>> getAll() {
//        return new ResponseEntity<>(.getAll(), HttpStatus.OK);
    }
}
