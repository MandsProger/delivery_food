package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.request.MenuEditRequest;
import com.springLesson.WebSpringLesson.services.ContentOrderService;
import com.springLesson.WebSpringLesson.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private ContentOrderService contentOrderService;

    @GetMapping("/menu")
    public String menuMain(Model model) {
        Iterable<Menu> menus = menuService.findAllMenu();
        model.addAttribute("menus", menus);
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
                              Long foodId) {
        menuService.menuEdit(foodId, menuEditRequest);
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
                                 @PathVariable(value = "foodId") Long foodId) {
        menuService.menuEdit(foodId, menuEditRequest);
        return "redirect:/menu";
    }

    @PostMapping("/menu/{foodId}/buy")
    public String menuBuy(@PathVariable(value = "foodId") Long foodId,
                          @RequestParam(value = "count", defaultValue = "1") int count,
                          @RequestParam(value = "action") String action) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        contentOrderService.addProductToCart(foodId, count, user.getNumberPhone());
        return "redirect:/menu";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/{foodId}/remove")
    public String menuPostRemove(@PathVariable(value = "foodId") Long foodId) {
        menuService.delete(foodId);
        return "redirect:/menu";
    }
}
