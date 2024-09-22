package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.repo.UserRepository;
import com.springLesson.WebSpringLesson.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.Period;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model,
                            @RequestParam String numberPhone) {
        String cleanPhone = cleanPhoneNumber(numberPhone);
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь уже сущетсвует");
            return "registration";
        }
        userService.saveUser(user);

        return "redirect:/login";
    }

    private String cleanPhoneNumber(String phone) {
        return phone.replaceAll("[^\\d]", ""); // Удаляем все нецифровые символы
    }

}
