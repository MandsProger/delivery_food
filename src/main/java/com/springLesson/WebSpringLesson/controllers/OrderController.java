package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.request.OrderPayRequest;
import com.springLesson.WebSpringLesson.services.ContentOrderService;
import com.springLesson.WebSpringLesson.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final ContentOrderService contentOrderService;

    @Autowired
    private final OrderService orderService;

    @GetMapping("/order")
    public String order(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<ContentOrder> cartItems = contentOrderService.getUserCart(user.getNumberPhone());
        float sum = 0;
        for (ContentOrder item : cartItems) {
            sum += item.getPrice();
        }
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", sum);
        return "order";
    }

    @PostMapping("/order/pay")
    public String orderPay(@ModelAttribute OrderPayRequest payRequest) {
        orderService.orderPay(payRequest);
        return "redirect:/menu";
    }

    @PostMapping("/order/{id}/remove")
    public String contentOrderRemoveProduct(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderDelete(id);
        return "redirect:/order";
    }
}
