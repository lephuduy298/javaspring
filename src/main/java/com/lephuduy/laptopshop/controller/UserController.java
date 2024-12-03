package com.lephuduy.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @RequestMapping("/")
    // public String getUserPage(Model model) {
    // List<User> arrUsers =
    // this.userService.getAllByEmail("leduyphucat@gmail.com");
    // System.out.println(arrUsers);
    // model.addAttribute("lephuduy", "test");
    // model.addAttribute("hoidanit", "hello model from jsp");
    // return "hello";
    // }

    // @RequestMapping("/admin/user")
    // public String getHomePage(Model model) {
    // model.addAttribute("newUser", new User());
    // return "/admin/user/create";
    // }

    // @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    // public String createHomePage(Model model, @ModelAttribute("newUser") User
    // lephuduy) {
    // System.out.println("this is inf of the form" + lephuduy);
    // this.userService.handleSaveUser(lephuduy);
    // return "hello";
    // }

    // @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    // public String createHomePage(Model model, @ModelAttribute("newUser") User
    // lephuduy) {
    // return "/admin/user/create";
    // }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/listuser";
    }

    @RequestMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUserPage(Model model, @ModelAttribute("newUser") User lephuduy) {
        System.out.println("this is inf of the form" + lephuduy);
        this.userService.handleSaveUser(lephuduy);
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/admin/user/{id}")
    public String createUserInfo(Model model, @PathVariable long id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "admin/user/show";
    }

    @RequestMapping(value = "/admin/user/update/{id}")
    public String updateUserInfo(Model model, @PathVariable long id) {
        model.addAttribute("newUser", this.userService.getUserById(id));
        return "admin/user/user-update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User lephuduy) {
        User currentUser = this.userService.getUserById(lephuduy.getId());
        if (currentUser != null) {
            currentUser.setPhone(lephuduy.getPhone());
            currentUser.setAddress(lephuduy.getAddress());
            currentUser.setFullName(lephuduy.getFullName());

            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String deleteUserInfo(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        return "admin/user/delete";
    }
}
