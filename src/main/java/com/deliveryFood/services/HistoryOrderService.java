package com.deliveryFood.services;

import com.deliveryFood.models.Order;
import com.deliveryFood.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryOrderService {

    private final OrderRepository orderRepository;

    public List<Order> getHistory(Long numberPhone) {
        return orderRepository.findAllIdByUserId(numberPhone);
    }
}
