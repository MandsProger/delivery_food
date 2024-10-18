package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.repo.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoryOrderService {

    private final ContentOrderRepository contentOrderRepository;
    private final OrderRepository orderRepository;

    public List<Order> getHistoryActiveCart(Long numberPhone) {
        return orderRepository.findAllIdByUserIdAndIsPaidFalse(numberPhone);
    }

    public List<Order> getHistoryArchiveCart(Long numberPhone) {
        return orderRepository.findAllIdByUserIdAndIsPaidTrue(numberPhone);
    }

    public void getAllHistoryOrder() {

    }
}
