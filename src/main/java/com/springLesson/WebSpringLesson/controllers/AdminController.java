package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    public final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.list());
        return "admin";
    }

    @PostMapping("/user/ban/{email}")
    public String userBan(@PathVariable("email") String email) {
        userService.banUser(email);
        return "redirect:/admin";
    }
}
