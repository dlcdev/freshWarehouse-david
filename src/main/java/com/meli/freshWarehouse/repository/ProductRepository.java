package com.meli.freshWarehouse.repository;

import com.meli.freshWarehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
