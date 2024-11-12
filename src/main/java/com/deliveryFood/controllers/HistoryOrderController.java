package com.deliveryFood.controllers;

import com.deliveryFood.models.ContentOrder;
import com.deliveryFood.models.Menu;
import com.deliveryFood.models.Order;
import com.deliveryFood.models.User;
import com.deliveryFood.services.ContentOrderService;
import com.deliveryFood.services.HistoryOrderService;
import com.deliveryFood.services.MenuService;
import com.deliveryFood.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HistoryOrderController {

    private final HistoryOrderService historyOrderService;
    private final OrderService orderService;
    private final MenuService menuService;

    @Autowired
    private final ContentOrderService contentOrderService;

    private static final Comparator<Order> ORDER_DATE_COMPARATOR =
            Comparator.comparing(Order::getDateOrder).reversed();

    @GetMapping("/orderHistory")
    public String orderHistory(@RequestParam(value = "filter", defaultValue = "active") String filter, Model model) {
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

        orderItems.sort(ORDER_DATE_COMPARATOR);

        model.addAttribute("user", user);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("filterStatus", filter);

        return "historyOrder";
    }

    @GetMapping("/orderHistory/{orderId}")
    public String currentOrderHistory(@PathVariable(value = "orderId") Long orderId, Model model) {
        Set<ContentOrder> items = contentOrderService.getAllItemsByOrderId(orderId);
        Optional<Order> orderOptional = orderService.getOrderById(orderId);

        Map<String, Menu> menusMap = new HashMap<>();

        List<Menu> allMenus = menuService.findAllMenu();
        for (Menu menu : allMenus) {
            menusMap.put(menu.getName(), menu);
        }

        if (!items.isEmpty() && orderOptional.isPresent()) {
            Order order = orderOptional.get();
            model.addAttribute("items", items);
            model.addAttribute("order", order);
            model.addAttribute("menusMap", menusMap);
            String formattedDate = orderService.getFormattedOrderDate(order.getDateOrder());
            model.addAttribute("formattedDate", formattedDate);
            return "currentOrderHistory";
        } else {
            return "redirect:/orderHistory";
        }
    }
}
