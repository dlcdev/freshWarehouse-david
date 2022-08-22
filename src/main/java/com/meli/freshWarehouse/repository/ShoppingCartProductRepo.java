package com.meli.freshWarehouse.repository;

import com.meli.freshWarehouse.model.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartProductRepo extends JpaRepository<ShoppingCartProduct, Long> {
}
