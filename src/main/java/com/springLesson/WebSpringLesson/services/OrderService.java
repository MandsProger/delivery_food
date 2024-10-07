package com.springLesson.WebSpringLesson.services;


import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.repo.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import com.springLesson.WebSpringLesson.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final ContentOrderRepository contentOrderRepository;

    /*private Order createNewOrder(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setDateOrder(LocalDateTime.now());
        order.setPaid(false);
        order.setNumberOrder(1);
        return orderRepository.save(order);
    }*/

    public Order getCurrentOrder(Long userId) {
        return orderRepository.findByUserIdAndIsPaidFalse(userId);
    }
}
