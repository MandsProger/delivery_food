package com.deliveryFood.repository;

import com.deliveryFood.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Menu, Integer> {

}
