package com.deliveryFood.services;

import com.deliveryFood.models.Menu;
import com.deliveryFood.models.Warehouse;
import com.deliveryFood.repository.WarehouseRepository;
import com.deliveryFood.request.WarehouseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final MenuService menuService;

    public List<Warehouse> findAllWarehouse() {
        return warehouseRepository.findAll();
    }

    public Set<Warehouse> findAllByWarehouseId(Long warehouseId) {
        return warehouseRepository.findAllByWarehouseId(warehouseId);
    }

    @Transactional
    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public void warehouseAdd(WarehouseRequest warehouseRequest) {
        if (warehouseRequest.getProduct() == null || warehouseRequest.getProduct().isEmpty() ||
                warehouseRequest.getQuantity() == null || warehouseRequest.getQuantity().isEmpty()) {
            throw new IllegalArgumentException("Product names or quantity lists cannot be null or empty");
        }

        Long warehouseId = generateUniqueWarehouseId();

        List<String> products = warehouseRequest.getProduct();
        List<Integer> quantities = warehouseRequest.getQuantity();

        List<Warehouse> warehouses = new ArrayList<>();
        List<Menu> menus = menuService.findAllMenu();

        Map<String, Menu> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            menuMap.put(menu.getName(), menu);
        }

        for (int i = 0; i < products.size(); i++) {
            Warehouse warehouse = new Warehouse();
            warehouse.setFoodName(products.get(i));
            warehouse.setCount(quantities.get(i));
            warehouse.setWarehouseId(warehouseId);
            warehouses.add(warehouse);

            Menu menu = menuMap.get(products.get(i));
            if (menu != null) {
                menu.setRemainder(menu.getRemainder() + quantities.get(i));
            }
        }

        menuService.saveMenus(menus);
        warehouseRepository.saveAll(warehouses);
    }

    private Long generateUniqueWarehouseId() {
        Long warehouseId;

        do {
            // Генерируем уникальный идентификатор, можно использовать более сложные стратегии по мере необходимости
            warehouseId = System.currentTimeMillis() + new Random().nextInt(1000);
        } while (warehouseRepository.existsByWarehouseId(warehouseId)); // Проверка уникальности

        return warehouseId;
    }
}

