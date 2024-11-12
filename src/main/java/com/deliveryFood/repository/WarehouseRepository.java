package com.deliveryFood.repository;


import com.deliveryFood.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Set<Warehouse> findAllByWarehouseId(Long warehouseId);
    boolean existsByWarehouseId(Long warehouseId);
}
