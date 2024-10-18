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

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HistoryOrderController {

    @Autowired
    private final HistoryOrderService historyOrderService;

    @GetMapping("/orderHistory")
    public String getOrderHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Order> orderItems = historyOrderService.getHistoryActiveCart(user.getNumberPhone());

        orderItems.sort(Comparator.comparing(Order::getDateOrder).reversed());

        model.addAttribute("user", user);
        model.addAttribute("orderItems", orderItems);

        return "historyOrder";
    }
}
