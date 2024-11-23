package com.lephuduy.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lephuduy.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getUserPage(Model model) {
        String test = this.userService.handleHello();
        model.addAttribute("lephuduy", test);
        model.addAttribute("hoidanit", "hello model from jsp");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getHomePage(Model model) {
        String test = this.userService.handleHello();
        model.addAttribute("lephuduy", test);
        model.addAttribute("hoidanit", "hello model from jsp");
        return "/admin/user/create";
    }
}

// @RestController
// public class UserController {

// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("")
// public String getHomePage() {
// return this.userService.handleHello();
// }
// }
