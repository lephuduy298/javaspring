package com.lephuduy.laptopshop.controller.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.service.UploadService;
import com.lephuduy.laptopshop.service.UserService;

import jakarta.servlet.ServletContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;

    public UserController(UploadService uploadService, UserService userService) {
        this.userService = userService;
        this.uploadService = uploadService;
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
        return "admin/user/show";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model, @ModelAttribute("newUser") User lephuduy,
            @RequestParam("lephuduyFile") MultipartFile file) {
        // this.userService.handleSaveUser(lephuduy);
        String avatar = this.uploadService.handleUploadFile(file, "avatar");
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/admin/user/{id}")
    public String createUserInfo(Model model, @PathVariable long id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "admin/user/detail";
    }

    @RequestMapping(value = "/admin/user/update/{id}")
    public String updateUserInfo(Model model, @PathVariable long id) {
        model.addAttribute("newUser", this.userService.getUserById(id));
        return "admin/user/update";
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
        // User user = new User();
        // user.setId(id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    // @Transactional nếu muốn dùng deleteAUser với hàm deleteUserById
    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User lephuduy) {
        // this.userService.deleteAUser(lephuduy.getId());
        this.userService.deleteAUser(lephuduy.getId());
        return "redirect:/admin/user";
    }
}
