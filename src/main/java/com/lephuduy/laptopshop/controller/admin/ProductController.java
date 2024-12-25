package com.lephuduy.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.service.ProductService;
import com.lephuduy.laptopshop.service.UploadService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> products = this.productService.fecthProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String createUserPage(Model model,
            @ModelAttribute("newProduct") @Valid Product lephuduy,
            BindingResult newProductBindingResult,
            @RequestParam("lephuduyProductFile") MultipartFile file) {

        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }

        String image = this.uploadService.handleUploadFile(file, "product");

        lephuduy.setImage(image);
        this.productService.handleSaveProduct(lephuduy);

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String createProductInf(Model model, @PathVariable long id) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "admin/product/detail";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProductInfo(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        // User user = new User();
        // user.setId(id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    // @Transactional nếu muốn dùng deleteAUser với hàm deleteUserById
    @PostMapping("/admin/product/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newProduct") Product lephuduy) {
        // this.userService.deleteAUser(lephuduy.getId());
        this.productService.deleteAProduct(lephuduy.getId());
        return "redirect:/admin/product";
    }

    @RequestMapping(value = "/admin/product/update/{id}")
    public String updateUserInfo(Model model, @PathVariable long id) {
        model.addAttribute("newProduct", this.productService.getProductById(id));
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("newProduct") @Valid Product lephuduy,
            BindingResult updateProductBindingResult,
            @RequestParam(value = "lephuduyProductFile", required = false) MultipartFile file) {

        List<FieldError> errors = updateProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        if (updateProductBindingResult.hasErrors()) {
            return "/admin/product/update";
        }
        Product currentProduct = this.productService.getProductById(lephuduy.getId());
        if (currentProduct != null) {
            currentProduct.setName(lephuduy.getName());
            currentProduct.setPrice(lephuduy.getPrice());
            currentProduct.setQuantity(lephuduy.getQuantity());
            currentProduct.setDetailDesc(lephuduy.getDetailDesc());
            currentProduct.setShortDesc(lephuduy.getShortDesc());
            currentProduct.setFactory(lephuduy.getFactory());

            // Update picture only if a new file is uploaded and not empty
            if (file != null && !file.isEmpty()) {
                String image = this.uploadService.handleUploadFile(file, "product");
                currentProduct.setImage(image);
            }
            this.productService.handleSaveProduct(currentProduct);

        }
        return "redirect:/admin/product";
    }

}
