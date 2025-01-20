package com.deliveryFood.controllers;

import com.deliveryFood.models.User;
import com.deliveryFood.models.enums.Role;
import com.deliveryFood.services.MenuService;
import com.deliveryFood.services.UserService;
import com.deliveryFood.services.WarehouseService;
import com.deliveryFood.models.Menu;
import com.deliveryFood.request.UserEditRequest;
import com.deliveryFood.request.WarehouseRequest;
import com.deliveryFood.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    public final UserService userService;
    public final OrderService orderService;
    public final WarehouseService warehouseService;
    public final MenuService menuService;

    @GetMapping("/admin/users")
    public String adminUser(Model model) {
        model.addAttribute("users", userService.list());
        return "adminUsersPanel";
    }

    @GetMapping("/admin/orderManagement")
    public String orderManagement(Model model) {
        model.addAttribute("orders", orderService.list());
        return "orderManagement";
    }

    @GetMapping("/admin/warehouse")
    public String warehouse(Model model) {
        model.addAttribute("warehouses", warehouseService.findAllWarehouse());
        return "warehouse";
    }


    @GetMapping("/admin/warehouse/add")
    public String warehouseAdd(Model model) {
        List<Menu> menus = menuService.findAllMenu();
        model.addAttribute("menus", menus);
        return "warehouseAdd";
    }

    @PostMapping("/admin/warehouse/add")
    public String menuPostAdd(@ModelAttribute WarehouseRequest warehouseRequest) {
        try {
            warehouseService.warehouseAdd(warehouseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/menu?error=true";
        }
        return "redirect:/admin/warehouse";
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

    @GetMapping("/admin/warehouse/{warehouseId}")
    public String getWarehouseId(@PathVariable(value = "warehouseId")
                                Long warehouseId, Model model) {
        model.addAttribute("warehouses", warehouseService.findAllByWarehouseId(warehouseId));
        return "warehouseId";
    }

    @GetMapping("/admin/users/edit/{numberPhone}")
    public String userEdit(@PathVariable(value = "numberPhone")
                                       Long numberPhone, Model model){
        User user = userService.getUserByNumberPhone(numberPhone);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
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

    @PostMapping("/admin/orderManagement/remove/{orderId}")
    public String orderPostRemove(@PathVariable(value = "orderId") Long orderId) {
        orderService.delete(orderId);
        return "redirect:/admin/orderManagement";
    }

    @PostMapping("/admin/orderManagement/finish/{orderId}")
    public String orderFinish(@PathVariable(value = "orderId") Long orderId) {
        orderService.orderFinish(orderId);
        return "redirect:/admin/orderManagement";
    }

}
