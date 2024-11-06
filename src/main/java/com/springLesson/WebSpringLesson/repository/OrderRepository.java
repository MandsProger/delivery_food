package com.springLesson.WebSpringLesson.repository;

import com.springLesson.WebSpringLesson.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserIdAndIsPaidFalse(Long userId);
    List<Order> findAllIdByUserIdAndIsPaidFalse(Long userId);
    List<Order> findAllIdByUserIdAndIsPaidTrue(Long userId);
    List<Order> findAllIdByUserId(Long userId);
}
