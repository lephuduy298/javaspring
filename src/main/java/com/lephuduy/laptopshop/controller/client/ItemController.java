package com.lephuduy.laptopshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.lephuduy.laptopshop.domain.CartDetail;
import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    private final ProductService productService;

    public ItemController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCard(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        long productId = id;

        String email = (String) session.getAttribute("email");

        this.productService.handleAddProductToCard(email, id, session);

        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPageString() {
        // lấy email người dùng
        // HttpSession session = request.getSession(false);
        // String email = (String) session.getAttribute("email");

        // List<CartDetail> cartDetails =
        // this.productService.getAllCartDetailByEmail(email);
        // model.addAttribute("cartDetails", cartDetails);
        return "client/cart/show";
    }

}
