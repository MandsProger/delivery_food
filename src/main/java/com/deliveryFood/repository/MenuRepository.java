package com.deliveryFood.repository;

import com.deliveryFood.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findOptionalByFoodId(Long foodId);
    Menu findByFoodId(Long foodId);
    Menu findByName(String name);
    List<Menu> findByNameIn(List<String> names);
    List<Menu> findAllByNameIn(List<String> names);
}
