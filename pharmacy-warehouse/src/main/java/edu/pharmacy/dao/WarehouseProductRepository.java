package edu.pharmacy.dao;

import edu.pharmacy.model.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {
}
