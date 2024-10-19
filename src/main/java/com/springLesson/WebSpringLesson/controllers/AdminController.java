package com.springLesson.WebSpringLesson.controllers;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.models.enums.Role;
import com.springLesson.WebSpringLesson.request.UserEditRequest;
import com.springLesson.WebSpringLesson.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    public final UserService userService;

    @GetMapping("/admin/users")
    public String adminUser(Model model) {
        model.addAttribute("users", userService.list());
        return "adminUsersPanel";
    }

    @GetMapping("/admin")
    public String adminPanel() {
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
        model.addAttribute("roles", user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet()));
        return "/userEdit";
    }

    @PostMapping("/admin/users/edit/{numberPhone}")
    public String userEditUpdate(@PathVariable(value = "numberPhone")
                                             Long numberPhone,
                                 @ModelAttribute UserEditRequest user) {
        userService.userUpdate(numberPhone, user);
        log.info("Пользователь {} изменен", user.getName());
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/remove/{numberPhone}")
    public String userPostRemove(@PathVariable(value = "numberPhone") Long numberPhone) {
        userService.delete(numberPhone);
        return "redirect:/admin/users";
    }

}
