package com.deliveryFood.services;

import com.deliveryFood.models.Order;
import com.deliveryFood.models.User;
import com.deliveryFood.repository.ContentOrderRepository;
import com.deliveryFood.repository.MenuRepository;
import com.deliveryFood.repository.OrderRepository;
import com.deliveryFood.repository.UserRepository;
import com.deliveryFood.request.OrderPayRequest;
import com.deliveryFood.models.ContentOrder;
import com.deliveryFood.models.Menu;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final ContentOrderRepository contentOrderRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + orderId + " не найден"));

        if (!order.isPaid()) {
            Set<String> foodNames = contentOrders.stream()
                    .map(ContentOrder::getFoodName)
                    .collect(Collectors.toSet());

            List<String> foodNamesList = new ArrayList<>(foodNames);
            List<Menu> menus = menuRepository.findAllByNameIn(foodNamesList);

            Map<String, Menu> menuMap = menus.stream()
                    .collect(Collectors.toMap(Menu::getName, menu -> menu));

            for (ContentOrder contentOrder : contentOrders) {
                Menu menu = menuMap.get(contentOrder.getFoodName());
                if (menu != null) {
                    menu.setRemainder(menu.getRemainder() + contentOrder.getCount());
                }
            }

            User user = userRepository.findByNumberPhone(order.getUserId());
            user.setBonus(user.getBonus() + order.getDiscount());

            menuRepository.saveAll(menus);
            userRepository.save(user);
        }
        orderRepository.deleteById(orderId);
    }

    public String getFormattedOrderDate(LocalDateTime dateOrder) {
        if (dateOrder == null) {
            return "Дата не указана";
        }
        return dateOrder.plusHours(1).format(FORMATTER);
    }

    @Transactional
    public void orderPay(OrderPayRequest orderPayRequest) {
        Order order = new Order();
        order.setResultPrice(orderPayRequest.getResultPrice());
        order.setUserId(orderPayRequest.getUserId());
        order.setCostDelivery(orderPayRequest.getCostDelivery());
        order.setPaymentMethod(orderPayRequest.getPaymentMethod());
        order.setOrderAddress(orderPayRequest.getOrderAddress());
        order.setComment(orderPayRequest.getComment());
        order.setDiscount(orderPayRequest.getDiscount());

        User user = userRepository.findByNumberPhone(order.getUserId());
        if (orderPayRequest.getDiscount() <= user.getBonus()) {
            user.setBonus(user.getBonus() - orderPayRequest.getDiscount());
        } else {
            throw new IllegalArgumentException("Бонусов не хватает. У вас " + user.getBonus() + " бонусов");
        }


        Set<ContentOrder> contentOrders = contentOrderRepository.findAllByUserIdAndOrderIdIsNull(orderPayRequest.getUserId());
        order.setContentOrders(contentOrders);
        List<String> foodNames = contentOrders.stream()
                .map(ContentOrder::getFoodName)
                .distinct()
                .collect(Collectors.toList());
        List<Menu> menus = menuRepository.findByNameIn(foodNames);
        Map<String, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getName, Function.identity()));

        for (ContentOrder contentOrder : contentOrders) {
            Menu menu = menuMap.get(contentOrder.getFoodName());
            if (menu == null) {
                throw new IllegalArgumentException("Товар с именем '" + contentOrder.getFoodName() + "' не найден в меню.");
            }
            if (menu.getRemainder() < contentOrder.getCount()) {
                throw new IllegalArgumentException("Недостаточно товара на складе для товара с именем '" + contentOrder.getFoodName() + "'.");
            }
            menu.setRemainder(menu.getRemainder() - contentOrder.getCount());
        }

        menuRepository.saveAll(menus);
        saveOrder(order);
        userRepository.save(user);
    }

    @Transactional
    public void orderFinish(Long orderId) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + orderId + " не найден"));
        order.setPaid(true);
        order.setOrderCompletionTime(LocalDateTime.now());
        if (order.getDiscount() == 0) {
            User user = userRepository.findByNumberPhone(order.getUserId());
            user.setBonus((int) (user.getBonus() + (order.getResultPrice() / 20)));
        }
        saveOrder(order);
    }
}
