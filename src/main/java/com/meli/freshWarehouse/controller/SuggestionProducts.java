package com.meli.freshWarehouse.controller;

import com.meli.freshWarehouse.dto.ProductSuggestionResponseDto;
import com.meli.freshWarehouse.dto.PurchaseOrderDto;
import com.meli.freshWarehouse.dto.PurchaseOrderResponseDTO;
import com.meli.freshWarehouse.dto.PurchaseOrderTotalPriceDTO;
import com.meli.freshWarehouse.model.PurchaseOrder;
import com.meli.freshWarehouse.service.PurchaseOrderService;
import com.meli.freshWarehouse.service.SuggestionProductsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/suggestions")
public class SuggestionProducts {

    @Autowired
    private SuggestionProductsService service;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public ResponseEntity<List<ProductSuggestionResponseDto>> getProductInCartByUser() {
        return new ResponseEntity<>(service.getAllProductInCart(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDTO> save(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        return new ResponseEntity<>(purchaseOrderService.save(purchaseOrderDto), HttpStatus.CREATED);
    }

    @PutMapping("/{purchaseOrderId}")
    public ResponseEntity<PurchaseOrderTotalPriceDTO> finalizePurchaseOrder(
            @PathVariable Long purchaseOrderId, PurchaseOrderDto purchaseOrderDto
    ) {
        return ResponseEntity.ok().body(purchaseOrderService.finalizePurchaseOrder(purchaseOrderId, purchaseOrderDto));
    }


}
