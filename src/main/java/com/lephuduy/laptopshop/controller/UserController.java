package com.lephuduy.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getUserPage(Model model) {
        model.addAttribute("lephuduy", "test");
        model.addAttribute("hoidanit", "hello model from jsp");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getHomePage(Model model) {
        model.addAttribute("newUser", new User());
        return "/admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createHomePage(Model model, @ModelAttribute("newUser") User lephuduy) {
        System.out.println("this is inf of the form" + lephuduy);
        this.userService.handleSaveUser(lephuduy);
        return "hello";
    }
}
