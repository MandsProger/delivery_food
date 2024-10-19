package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.services.ContentOrderService;
import com.springLesson.WebSpringLesson.services.MenuService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ContentOrderController {

    @Autowired
    private final ContentOrderService contentOrderService;

    @Autowired
    private final MenuService menuService;

    @GetMapping("/contentOrder")
    public String contentOrderUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<ContentOrder> cartItems = contentOrderService.getUserCart(user.getNumberPhone());
        float sum = 0;
        for (ContentOrder item : cartItems) {
            sum += item.getPrice();
        }
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", sum);

        return "contentOrder";
    }

    @PostMapping("/contentOrder/{id}/remove")
    public String contentOrderRemoveProduct(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderDelete(id);
        return "redirect:/contentOrder";
    }
}
