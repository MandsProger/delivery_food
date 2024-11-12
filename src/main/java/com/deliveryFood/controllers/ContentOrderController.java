package com.deliveryFood.controllers;

import com.deliveryFood.models.ContentOrder;
import com.deliveryFood.models.Menu;
import com.deliveryFood.models.User;
import com.deliveryFood.services.ContentOrderService;
import com.deliveryFood.services.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ContentOrderController {

    private final ContentOrderService contentOrderService;
    private final MenuService menuService;

    @GetMapping("/contentOrder")
    public String contentOrderUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<ContentOrder> cartItems = contentOrderService.getAllUserCartByNumberPhone(user.getNumberPhone());
        Map<String, Menu> menusMap = new HashMap<>();

        List<Menu> allMenus = menuService.findAllMenu();
        for (Menu menu : allMenus) {
            menusMap.put(menu.getName(), menu);
        }
        float sum = 0;
        for (ContentOrder item : cartItems) {
            sum += item.getPrice();
        }
        model.addAttribute("user", user);
        model.addAttribute("menusMap", menusMap);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", sum);

        return "contentOrder";
    }

    @PostMapping("/contentOrder/{id}/remove")
    public String contentOrderRemoveProduct(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderDelete(id);
        return "redirect:/contentOrder";
    }

    @PostMapping("/contentOrder/{id}/minus")
    public String contentOrderMinusProduct(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderMinus(id);
        return "redirect:/contentOrder";
    }

    @PostMapping("/contentOrder/{id}/plus")
    public String contentOrderMinusPlus(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderPlus(id);
        return "redirect:/contentOrder";
    }
}
