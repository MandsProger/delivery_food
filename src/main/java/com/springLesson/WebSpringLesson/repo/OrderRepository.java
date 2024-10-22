package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserIdAndIsPaidFalse(Long userId);
    List<Order> findAllIdByUserIdAndIsPaidFalse(Long userId);
    List<Order> findAllIdByUserIdAndIsPaidTrue(Long userId);
    List<Order> findAllIdByUserId(Long userId);
}
