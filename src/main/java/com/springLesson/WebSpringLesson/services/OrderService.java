package com.springLesson.WebSpringLesson.services;


import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.repo.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import com.springLesson.WebSpringLesson.repo.OrderRepository;
import com.springLesson.WebSpringLesson.request.OrderPayRequest;
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

    public Order getCurrentOrder(Long userId) {
        return orderRepository.findByUserIdAndIsPaidFalse(userId);
    }

    public Order saveOrder(Order order) {
       return orderRepository.save(order);
    }

    public void orderPay(OrderPayRequest orderPayRequest/*, Long userId*/) {
        Order order = new Order();
        //ContentOrder contentOrder = contentOrderRepository.findAllByUserIdAndOrderIdNull(userId);

        order.setResultPrice(orderPayRequest.getResultPrice());
        order.setUserId(orderPayRequest.getUserId());
        order.setCostDelivery(orderPayRequest.getCostDelivery());
        order.setPaymentMethod(orderPayRequest.getPaymentMethod());

        saveOrder(order);
    }
}
