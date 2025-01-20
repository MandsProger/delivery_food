package com.deliveryFood.controllers;

import com.deliveryFood.models.ContentOrder;
import com.deliveryFood.models.Menu;
import com.deliveryFood.models.User;
import com.deliveryFood.request.AddressRequest;
import com.deliveryFood.request.OrderPayRequest;
import com.deliveryFood.services.ContentOrderService;
import com.deliveryFood.services.MenuService;
import com.deliveryFood.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ContentOrderService contentOrderService;
    private final OrderService orderService;
    private final MenuService menuService;

    @GetMapping("/order")
    public String order(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<ContentOrder> cartItems = contentOrderService.getAllUserCartByNumberPhone(user.getNumberPhone());

        Map<String, Menu> menusMap = new HashMap<>();

        List<Menu> allMenus = menuService.findAllMenu();
        for (Menu menu : allMenus) {
            menusMap.put(menu.getName(), menu);
        }

        if (cartItems.isEmpty()) {
            return "redirect:/contentOrder";
        }

        float sum = 0;
        for (ContentOrder item : cartItems) {
            sum += item.getPrice();
        }
        LocalDateTime currentTimePlusOneHour = LocalDateTime.now().plusHours(1);
        model.addAttribute("currentTime", currentTimePlusOneHour);
        model.addAttribute("user", user);
        model.addAttribute("menusMap", menusMap);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sum", sum);
        return "order";
    }

    @PostMapping("/order/pay")
    public String orderPay(@ModelAttribute OrderPayRequest payRequest,
                           @ModelAttribute AddressRequest addressRequest, RedirectAttributes redirectAttributes) {
        String orderAddress = String.format("%s, дом: %s, Кв/офис: %s, Домофон: %s, Подъезд: %s, Этаж: %s",
                addressRequest.getStreet(),
                addressRequest.getHouse(),
                addressRequest.getApartment(),
                addressRequest.getIntercom(),
                addressRequest.getPorch(),
                addressRequest.getFloor());
        try {
            payRequest.setOrderAddress(orderAddress);
            orderService.orderPay(payRequest);
            return "redirect:/orderHistory";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/contentOrder";
        }
    }

    @PostMapping("/order/{id}/remove")
    public String contentOrderRemoveProduct(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderDelete(id);
        return "redirect:/order";
    }

    @PostMapping("/order/{id}/minus")
    public String contentOrderMinusProduct(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderMinus(id);
        return "redirect:/order";
    }

    @PostMapping("/order/{id}/plus")
    public String contentOrderMinusPlus(@PathVariable(value = "id") Long id) {
        contentOrderService.contentOrderPlus(id);
        return "redirect:/order";
    }
}
