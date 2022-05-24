package edu.pharmacy.dao;

import edu.pharmacy.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    boolean existsByPhone(String phone);

    @Query(value = "SELECT * FROM warehouses w WHERE w.pharmacy_id =?1", nativeQuery = true)
    Warehouse getByPharmacyId(long pharmacyId);
}
