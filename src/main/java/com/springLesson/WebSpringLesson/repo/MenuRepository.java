package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Menu findByFoodId(Integer foodId);
}
