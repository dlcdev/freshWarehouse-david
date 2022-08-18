package com.meli.freshWarehouse.repository;

import com.meli.freshWarehouse.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepo extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findAllByOrderStatusEndingWith(String status);
}
