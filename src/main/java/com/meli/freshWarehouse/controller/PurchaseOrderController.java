package com.meli.freshWarehouse.controller;

import com.meli.freshWarehouse.dto.PurchaseOrderDto;
import com.meli.freshWarehouse.dto.PurchaseOrderResponseDTO;
import com.meli.freshWarehouse.dto.PurchaseOrderTotalPriceDTO;
import com.meli.freshWarehouse.model.PurchaseOrder;
import com.meli.freshWarehouse.service.ProductService;
import com.meli.freshWarehouse.service.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fresh-products/purchase-order")
public class PurchaseOrderController {

    private final ProductService productService;
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(ProductService productService, PurchaseOrderService purchaseOrderService) {
        this.productService = productService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDTO> save(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        return new ResponseEntity<>(purchaseOrderService.save(purchaseOrderDto), HttpStatus.CREATED);
    }

    @GetMapping("/{purchaseOrderId}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long purchaseOrderId) {
        return ResponseEntity.ok().body(purchaseOrderService.getById(purchaseOrderId));
    }

    @PutMapping("/{purchaseOrderId}")
    public ResponseEntity<PurchaseOrderTotalPriceDTO> finalizePurchaseOrder(@PathVariable Long purchaseOrderId) {
        return ResponseEntity.ok().body(purchaseOrderService.finalizePurchaseOrder(purchaseOrderId));
    }
}
