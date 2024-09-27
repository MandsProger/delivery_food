package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.models.enums.Role;
import com.springLesson.WebSpringLesson.request.UserEditRequest;
import com.springLesson.WebSpringLesson.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    public final UserService userService;

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

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/admin/users/edit/{numberPhone}")
    public String userEditUpdate(@PathVariable(value = "numberPhone")
                                             Long numberPhone, Model model,
                                 @ModelAttribute UserEditRequest user) {
        userService.userUpdate(numberPhone, user);
        return "redirect:/admin/users";
    }

}
