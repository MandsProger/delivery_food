package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import com.springLesson.WebSpringLesson.repo.UserRepository;
import com.springLesson.WebSpringLesson.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    public final UserService userService;
    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin/users")
    public String adminUser(Model model) {
        model.addAttribute("users", userService.list());
        return "adminUsersPanel";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        return "adminPanel";
    }

    @PostMapping("/admin/users/ban/{numberPhone}")
    public String userBan(@PathVariable(value = "numberPhone") Long numberPhone) {
        userService.banUser(numberPhone);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{numberPhone}")
    public String userEdit(@PathVariable(value = "numberPhone")
                                       Long numberPhone, Model model){
        User user = userService.getUserByNumberPhone(numberPhone);
        ArrayList<User> users = new ArrayList<>();
        if (user != null) {
            users.add(user);
        } else return "redirect:/admin/user";

        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "/userEdit";
    }

    @PostMapping("/admin/users/edit/{numberPhone}")
    public String userEditUpdate(@PathVariable(value = "numberPhone")
                                             Long numberPhone, Model model,
                                 @RequestParam String name, @RequestParam String email,
                                 @RequestParam int bonus, @RequestParam String password,
                                 @RequestParam("gender") Gender gender, @RequestParam("roles") String[] roleNames) {

        User user = userRepository.findByNumberPhone(numberPhone);
        user.setName(name);
        user.setEmail(email);
        user.setBonus(bonus);
        if (password != "") {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setGender(gender);


        Set<Role> roles = new HashSet<>();
        for (String role : roleNames) {
            roles.add(Role.valueOf(role));
        }
        user.setRoles(roles);


        userRepository.save(user);

        return "redirect:/admin/users";
    }
}
