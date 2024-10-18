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
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final ContentOrderRepository contentOrderRepository;

    @Transactional
    public Order saveOrder(Order order) {
       return orderRepository.save(order);
    }

    @Transactional
    public void orderPay(OrderPayRequest orderPayRequest) {
        Order order = new Order();
        order.setResultPrice(orderPayRequest.getResultPrice());
        order.setUserId(orderPayRequest.getUserId());
        order.setCostDelivery(orderPayRequest.getCostDelivery());
        order.setPaymentMethod(orderPayRequest.getPaymentMethod());
        order.setOrderAddress(orderPayRequest.getOrderAddress());
        Set<ContentOrder> contentOrder = contentOrderRepository.findAllByUserIdAndOrderIdIsNull(orderPayRequest.getUserId());
        order.setContentOrders(contentOrder);

        saveOrder(order);
    }
}
