package com.lephuduy.laptopshop.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.domain.dto.RegisterDTO;
import com.lephuduy.laptopshop.service.ProductService;
import com.lephuduy.laptopshop.service.UserService;

import jakarta.validation.Valid;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.fecthAllProducts();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "/client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult) {

        /// validate
        if (bindingResult.hasErrors()) {
            return "/client/auth/register";
        }

        User user = this.userService.registerOTDtoUser(registerDTO);
        String hashPassword = this.passwordEncoder.encode(registerDTO.getPassword());
        user.setPassword(hashPassword);

        user.setRole(this.userService.getRoleByName("USER"));
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        // model.addAttribute("registerUser", new RegisterDTO());
        return "/client/auth/login";
    }

    @GetMapping("/access_deny")
    public String getDenyPage(Model model) {
        // model.addAttribute("registerUser", new RegisterDTO());
        return "/client/auth/access_deny";
    }

}
