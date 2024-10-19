package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.services.HistoryOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HistoryOrderController {

    @Autowired
    private final HistoryOrderService historyOrderService;

    @GetMapping("/orderHistory")
    public String getOrderHistory(@RequestParam(value = "filter", defaultValue = "active") String filter, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Order> orderItems = historyOrderService.getHistory(user.getNumberPhone());

        if ("active".equals(filter)) {
            orderItems = orderItems.stream()
                    .filter(order -> !order.isPaid())
                    .collect(Collectors.toList());
        } else if ("completed".equals(filter)) {
            orderItems = orderItems.stream()
                    .filter(order -> order.isPaid())
                    .collect(Collectors.toList());
        }

        orderItems.sort(Comparator.comparing(Order::getDateOrder).reversed());

        model.addAttribute("user", user);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("filterStatus", filter);

        return "historyOrder";
    }
}
