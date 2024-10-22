package com.springLesson.WebSpringLesson.services;


import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.repo.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import com.springLesson.WebSpringLesson.repo.OrderRepository;
import com.springLesson.WebSpringLesson.request.OrderPayRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public void delete(Long orderId) {
        Set<ContentOrder> contentOrders = contentOrderRepository.findAllByOrderId(orderId);
        for (ContentOrder contentOrder : contentOrders) {
            Menu menu = menuRepository.findByName(contentOrder.getFoodName());
            if (menu != null) {
                menu.setRemainder(menu.getRemainder() + contentOrder.getCount());
                menuRepository.save(menu);
            }
        }
        orderRepository.deleteById(orderId);
    }

    public String getFormattedOrderDate(LocalDateTime dateOrder) {
        if (dateOrder == null) {
            return "Дата не указана";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateOrder.plusHours(1).format(formatter);
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

    @Transactional
    public void orderFinish(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        Order order1 = order.get();
        order1.setPaid(true);
        order1.setOrderCompletionTime(LocalDateTime.now());
        saveOrder(order1);
    }
}
