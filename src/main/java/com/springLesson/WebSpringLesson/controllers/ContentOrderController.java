package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Order;
import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.services.ContentOrderService;
import com.springLesson.WebSpringLesson.services.OrderService;
import com.springLesson.WebSpringLesson.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ContentOrderController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ContentOrderService contentOrderService;

    @Autowired
    private final OrderService orderService;

    @GetMapping("/contentOrder")
    public String contentOrderUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<ContentOrder> cartItems = contentOrderService.getUserCart(user.getNumberPhone());
        model.addAttribute("cartItems", cartItems);

        return "contentOrder";
    }
}
