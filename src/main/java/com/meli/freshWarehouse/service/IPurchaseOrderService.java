package com.meli.freshWarehouse.service;

import com.meli.freshWarehouse.dto.PurchaseOrderDto;
import com.meli.freshWarehouse.dto.PurchaseOrderResponseDTO;
import com.meli.freshWarehouse.dto.PurchaseOrderTotalPriceDTO;
import com.meli.freshWarehouse.model.PurchaseOrder;

public interface IPurchaseOrderService {
    PurchaseOrderResponseDTO save(PurchaseOrderDto purchaseOrderDto);
    PurchaseOrder getById(Long purchaseOrderId);
    PurchaseOrderTotalPriceDTO finalizePurchaseOrder(Long purchaseOrderId);
}
