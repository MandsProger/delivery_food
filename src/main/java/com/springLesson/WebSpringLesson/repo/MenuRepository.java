package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findOptionalByFoodId(Long foodId);
    Menu findByFoodId(Long foodId);
    Menu findByName(String name);
}
