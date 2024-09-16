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
    private final UserRepository userRepository;

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
                            @RequestParam String number_phone,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String gender) {
        String cleanPhone = cleanPhoneNumber(number_phone);
        if (!userService.creatUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже сущетсвует");
            return "registration";
        }
        userRepository.save(user);

        return "redirect:/login";
    }

    private String cleanPhoneNumber(String phone) {
        return phone.replaceAll("[^\\d]", ""); // Удаляем все нецифровые символы
    }

}
