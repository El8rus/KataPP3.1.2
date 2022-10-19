package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "admin";
    }

    @GetMapping("/new_user")
    public String newUser(@ModelAttribute("userForm") User user, Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "new_user";
    }

    @PostMapping("/new_user")
    public String addUser(@ModelAttribute("userForm") @Valid User newUser, BindingResult bindingResult, Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        if (bindingResult.hasErrors()) {
            return "new_user";
        }
        userService.save(newUser);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User editUser = userService.findUserById(id);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("editUser", editUser);
        model.addAttribute("roles", roles);
        return "edit_user";
    }

    @PatchMapping("/{id}")
    public String saveUser(@PathVariable("id") Long id, @ModelAttribute("editUser") @Valid User editUser,
                           BindingResult bindingResult, Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        if (bindingResult.hasErrors()) {
            return "edit_user";
        }
        userService.save(editUser);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
