package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.repo.OrderRepository;
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
