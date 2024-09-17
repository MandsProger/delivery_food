package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model, User user) {
        model.addAttribute("t", user.getRoles());
        model.addAttribute("title", "Главная страница");

        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

}