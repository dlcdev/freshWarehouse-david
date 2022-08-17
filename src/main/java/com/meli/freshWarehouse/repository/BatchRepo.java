package com.meli.freshWarehouse.repository;

import com.meli.freshWarehouse.dto.ProductSuggestionDto;
import com.meli.freshWarehouse.dto.WarehouseForProductStockResponseDTO;
import com.meli.freshWarehouse.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepo extends JpaRepository<Batch, Long> {
    @Query("SELECT DISTINCT new com.meli.freshWarehouse.dto.WarehouseForProductStockResponseDTO(s.warehouse.id, SUM(b.currentQuantity)) FROM Batch b LEFT JOIN Section s ON b.section.id = s.id WHERE b.product.id = :productId GROUP BY s.warehouse.id")
    List<WarehouseForProductStockResponseDTO> getStockOfProductById(@Param("productId") Long productId);

    @Query(value = "SELECT new com.meli.freshWarehouse.dto.ProductSuggestionDto(p.id, p.name, b.currentQuantity, p.price) " +
            "FROM Batch b " +
            "INNER JOIN Product p ON b.product.id = p.id " +
            "INNER JOIN Section s on b.section.id = s.id where s.name = :sectionName"
    )
    List<ProductSuggestionDto> getStockBySectionName(@Param("sectionName") String sectionName);

}
