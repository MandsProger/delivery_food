package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/menu")
    public String menuMain(Model model) {
        Iterable<Menu> menus = menuRepository.findAll();
        model.addAttribute("menus", menus);
        return "menuMain";
    }

    @GetMapping("/menu/add")
    public String menuAdd(Model model) {
        return "menuAdd";
    }

    @PostMapping("/menu/add")
    public String menuPostAdd(@RequestParam String name, @RequestParam int price,
                              @RequestParam String category, @RequestParam int remainder,
                              @RequestParam String description, @RequestParam String volume, Model model) {
        Menu menu = new Menu(price, remainder, name, category, description, volume);
        menuRepository.save(menu);
        return "redirect:/menu";
    }

    @GetMapping("/menu/{food_id}")
    public String menuDetails(@PathVariable(value = "food_id") int food_id, Model model) {
        if (!menuRepository.existsById(food_id)) {
            return "redirect:/menu";
        }
        Optional<Menu> menu = menuRepository.findById(food_id);
        ArrayList<Menu> menus = new ArrayList<>();
        menu.ifPresent(menus::add);
        model.addAttribute("menu", menus);
        return "menuDetails";
    }

    @GetMapping("/menu/{food_id}/edit")
    public String menuEdit(@PathVariable(value = "food_id") int food_id, Model model) {
        if (!menuRepository.existsById(food_id)) {
            return "redirect:/menu";
        }
        Optional<Menu> menu = menuRepository.findById(food_id);
        ArrayList<Menu> menus = new ArrayList<>();
        menu.ifPresent(menus::add);
        model.addAttribute("menu", menus);
        return "menuEdit";
    }

    @PostMapping("/menu/{food_id}/edit")
    public String menuPostUpdate(@PathVariable(value = "food_id") int food_id,
                                 @RequestParam String name, @RequestParam int price,
                              @RequestParam String category, @RequestParam int remainder,
                              @RequestParam String description, @RequestParam String volume, Model model) {
        Menu menu = menuRepository.findById(food_id).orElseThrow();
        menu.setName(name);
        menu.setPrice(price);
        menu.setCategory(category);
        menu.setRemainder(remainder);
        menu.setDescription(description);
        menu.setVolume(volume);
        menuRepository.save(menu);
        return "redirect:/menu";
    }

    @PostMapping("/menu/{food_id}/remove")
    public String menuPostRemove(@PathVariable(value = "food_id") int food_id, Model model) {
        Menu menu = menuRepository.findById(food_id).orElseThrow();
        menuRepository.delete(menu);
        return "redirect:/menu";
    }
}
