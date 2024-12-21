package com.lephuduy.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.service.UploadService;
import com.lephuduy.laptopshop.service.UserService;

import jakarta.validation.Valid;

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
    private final PasswordEncoder passwordEncoder;

    public UserController(UploadService uploadService, UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
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
    public String createUserPage(Model model,
            @ModelAttribute("newUser") @Valid User lephuduy,
            BindingResult newUserBindingResult,
            @RequestParam("lephuduyFile") MultipartFile file) {

        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        if (newUserBindingResult.hasErrors()) {
            return "/admin/user/create";
        }

        String avatar = this.uploadService.handleUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(lephuduy.getPassword());

        lephuduy.setAvatar(avatar);
        lephuduy.setPassword(hashPassword);
        lephuduy.setRole(this.userService.getRoleByName(lephuduy.getRole().getName()));
        this.userService.handleSaveUser(lephuduy);

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
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User lephuduy,
            @RequestParam(value = "lephuduyFile", required = false) MultipartFile file) {
        User currentUser = this.userService.getUserById(lephuduy.getId());
        if (currentUser != null) {
            currentUser.setPhone(lephuduy.getPhone());
            currentUser.setAddress(lephuduy.getAddress());
            currentUser.setFullName(lephuduy.getFullName());
            currentUser.setRole(this.userService.getRoleByName(lephuduy.getRole().getName()));

            // Update picture only if a new file is uploaded and not empty
            if (file != null && !file.isEmpty()) {
                String avatar = this.uploadService.handleUploadFile(file, "avatar");
                currentUser.setAvatar(avatar);
            }
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
