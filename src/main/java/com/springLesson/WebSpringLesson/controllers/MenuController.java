package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.request.MenuEditRequest;
import com.springLesson.WebSpringLesson.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/menu")
    public String menuMain(Model model) {
        Iterable<Menu> menus = menuService.findAllMenu();
        model.addAttribute("menus", menus);
        return "menuMain";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/menu/add")
    public String menuAdd(Model model) {
        return "menuAdd";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/add")
    public String menuPostAdd(@ModelAttribute MenuEditRequest menuEditRequest,
                              Long foodId, Model model) {
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
                                 @PathVariable(value = "foodId") Long foodId, Model model) {
        menuService.menuEdit(foodId, menuEditRequest);
        return "redirect:/menu";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/{foodId}/remove")
    public String menuPostRemove(@PathVariable(value = "foodId") Long foodId, Model model) {
        Menu menu = menuService.findByMenuId(foodId);
        menuService.delete(menu);
        return "redirect:/menu";
    }
}
