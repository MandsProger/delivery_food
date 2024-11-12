package com.deliveryFood.controllers;

import com.deliveryFood.models.User;
import com.deliveryFood.services.MenuService;
import com.deliveryFood.models.ContentOrder;
import com.deliveryFood.models.Menu;
import com.deliveryFood.request.MenuEditRequest;
import com.deliveryFood.services.ContentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final ContentOrderService contentOrderService;

    @GetMapping("/menu")
    public String menuMain(Model model) {
        Iterable<Menu> menus = menuService.findAllMenu();
        model.addAttribute("menus", menus);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Set<ContentOrder> contentOrders = contentOrderService.getAllUserCartByNumberPhone(user.getNumberPhone());
        model.addAttribute("contentOrders", contentOrders);
        return "menuMain";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/menu/add")
    public String menuAdd() {
        return "menuAdd";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/add")
    public String menuPostAdd(@ModelAttribute MenuEditRequest menuEditRequest,
                              @RequestParam("image") MultipartFile image) {
        try {
            menuService.menuEdit(null, menuEditRequest, image);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/menu?error=true";
        }
        return "redirect:/menu";
    }

    @GetMapping("/menu/{foodId}")
    public String menuDetails(@PathVariable(value = "foodId") Long foodId, Model model) {
        return menuService.findOptionalByMenuId(foodId).map(menu -> {
            model.addAttribute("menu", List.of(menu));
            return "menuDetails";
        }).orElseGet(() -> "redirect:/menu");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/menu/{foodId}/edit")
    public String menuEdit(@PathVariable(value = "foodId") Long foodId, Model model) {
        return menuService.findOptionalByMenuId(foodId).map(menu -> {
            model.addAttribute("menu", List.of(menu));
            return "menuEdit";
        }).orElseGet(() -> "redirect:/menu");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/{foodId}/edit")
    public String menuPostUpdate(@ModelAttribute MenuEditRequest menuEditRequest,
                                 @PathVariable(value = "foodId") Long foodId,
                                 @RequestParam("image") MultipartFile image) {
        try {
            menuService.menuEdit(foodId, menuEditRequest, image);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/menu?error=true";
        }
        return "redirect:/menu";
    }

    @PostMapping("/menu/{foodId}/buy")
    public String menuBuy(@PathVariable(value = "foodId") Long foodId,
                          @RequestParam(value = "count", defaultValue = "1") int count, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        try {
            contentOrderService.addProductToCart(foodId, count, user.getNumberPhone());
            return "redirect:/menu";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        Iterable<Menu> menus = menuService.findAllMenu();
        model.addAttribute("menus", menus);

        Set<ContentOrder> contentOrders = contentOrderService.getAllUserCartByNumberPhone(user.getNumberPhone());
        model.addAttribute("contentOrders", contentOrders);
        return "menuMain";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/{foodId}/remove")
    public String menuPostRemove(@PathVariable(value = "foodId") Long foodId) {
        menuService.delete(foodId);
        return "redirect:/menu";
    }
}
