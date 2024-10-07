package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserIdAndIsPaidFalse(Long userId);
}
