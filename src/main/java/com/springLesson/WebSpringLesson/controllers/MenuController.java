package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

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
    public String menuPostAdd(@RequestParam String name, @RequestParam int price,
                              @RequestParam String category, @RequestParam int remainder,
                              @RequestParam String description, @RequestParam String volume, Model model) {
        menuService.menuAddBuild(name, price, category, remainder, description, volume);
        return "redirect:/menu";
    }

    @GetMapping("/menu/{foodId}")
    public String menuDetails(@PathVariable(value = "foodId") int foodId, Model model) {
        if (!menuService.existsMenuById(foodId)) {
            return "redirect:/menu";
        }
        menuService.findOptionalByMenuId(foodId).ifPresent(menu ->
                model.addAttribute("menu", List.of(menu)));
        return "menuDetails";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/menu/{foodId}/edit")
    public String menuEdit(@PathVariable(value = "foodId") int foodId, Model model) {
        if (!menuService.existsMenuById(foodId)) {
            return "redirect:/menu";
        }
        menuService.findOptionalByMenuId(foodId).ifPresent(menu ->
                model.addAttribute("menu", List.of(menu)));
        return "menuEdit";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/{foodId}/edit")
    public String menuPostUpdate(@PathVariable(value = "foodId") int foodId,
                                 @RequestParam String name, @RequestParam int price,
                              @RequestParam String category, @RequestParam int remainder,
                              @RequestParam String description, @RequestParam String volume, Model model) {
        menuService.menuEditBuild(foodId, name, price, category, remainder, description, volume);
        return "redirect:/menu";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu/{foodId}/remove")
    public String menuPostRemove(@PathVariable(value = "foodId") int foodId, Model model) {
        Menu menu = menuService.findByMenuId(foodId);
        menuService.delete(menu);
        return "redirect:/menu";
    }
}
