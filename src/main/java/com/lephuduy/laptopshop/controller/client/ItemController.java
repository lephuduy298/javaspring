package com.lephuduy.laptopshop.controller.client;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.lephuduy.laptopshop.domain.Cart;
import com.lephuduy.laptopshop.domain.CartDetail;
import com.lephuduy.laptopshop.domain.Order;
import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.Product_;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.domain.dto.ProductCriteriaDTO;
import com.lephuduy.laptopshop.repository.CartRepository;
import com.lephuduy.laptopshop.repository.OrderRepository;
import com.lephuduy.laptopshop.service.OrderService;
import com.lephuduy.laptopshop.service.ProductService;
import com.lephuduy.laptopshop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ItemController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public ItemController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
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
    public String getCartPageString(Model model, HttpServletRequest request) {
        // lấy email người dùng
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        Cart cart = this.productService.findByUser(user);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", cart);

        return "client/cart/show";
    }

    @PostMapping("/delete-product-from-cart/{id}")
    public String deleteProductFromCart(@PathVariable long id, HttpServletRequest request) {
        // TODO: process POST request
        long cartDetailId = id;
        HttpSession session = request.getSession(false);
        this.productService.deleteProductFromCart(cartDetailId, session);

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, HttpServletRequest request) {
        // lấy email người dùng
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        Cart cart = this.productService.findByUser(user);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", cart);

        return "client/cart/checkout";
    }

    @PostMapping("/confirm-checkout")
    public String confirmCheckout(@ModelAttribute("cart") Cart cart) {
        // TODO: process POST request
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckOut(cartDetails);

        return "redirect:/checkout";
    }

    @PostMapping("/place-order")
    public String handlePlaceOrder(HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {
        // TODO: process POST request
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        User currentUser = new User();
        currentUser.setId(id);

        this.productService.handlePlaceOrder(session, currentUser, receiverName, receiverAddress, receiverPhone);

        return "redirect:/thanks";
    }

    @GetMapping("/thanks")
    public String getThanksPage() {

        return "client/cart/thanks";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);

        List<Order> orders = this.orderService.getAllByUser(user);

        model.addAttribute("orders", orders);

        return "client/cart/order-history";
    }

    @GetMapping("/client/product/show")
    public String getMethodName(Model model, ProductCriteriaDTO productCriteriaDTO, HttpServletRequest request) {

        // create pageable
        int page = 1;
        if (productCriteriaDTO.getPage() != null) {
            page = productCriteriaDTO.getPage().isPresent() ? Integer.parseInt(productCriteriaDTO.getPage().get())
                    : 1;
        }

        // create sort
        Pageable pageable = null;
        if (productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()) {
            String sort = productCriteriaDTO.getSort().get();
            if (sort.equals("gia-tang-dan")) {
                pageable = PageRequest.of(page - 1, 3, Sort.by(Product_.PRICE).ascending());

            } else if (sort.equals("gia-giam-dan")) {
                pageable = PageRequest.of(page - 1, 3, Sort.by(Product_.PRICE).descending());

            } else {
                pageable = PageRequest.of(page - 1, 3);

            }
        } else {
            pageable = PageRequest.of(page - 1, 3);
        }

        Page<Product> products = this.productService.fecthProductsWithSpec(pageable, productCriteriaDTO);

        // name
        // String name = productCriteriaDTO.isPresent() ? productCriteriaDTO.get() : "";
        // Page<Product> products =
        // this.productService.fecthProductsWithSpec(pageable,name);

        // min-price
        // Double minPrice = minPriceOptional.isPresent() ?
        // Double.parseDouble(minPriceOptional.get()) : 0;
        // Page<Product> products = this.productService.fecthProductsWithSpec(pageable,
        // minPrice);

        // max price
        // Double maxPrice = maxPriceOptional.isPresent() ?
        // Double.parseDouble(maxPriceOptional.get()) : 0;
        // Page<Product> products = this.productService.fecthProductsWithSpec(pageable,
        // maxPrice);

        // factory
        // String factory = factoryOptional.isPresent() ? factoryOptional.get() : "";
        // Page<Product> products = this.productService.fecthProductsWithSpec(pageable,
        // factory);

        // in factory
        // List<String> factory = Arrays.asList(factoryOptional.get().split(","));
        // Page<Product> products = this.productService.fecthProductsWithSpec(pageable,
        // factory);

        // in range of price
        // List<String> price = Arrays.asList(priceOptional.get().split(","));
        // Page<Product> products = this.productService.fecthProductsWithSpec(pageable,
        // price);

        List<Product> listProducts = products.getContent();

        String qs = request.getQueryString();
        // if (qs != null && !qs.isBlank()) {
        // qs = qs.replace("page=" + page, "");
        // }
        model.addAttribute("products", listProducts);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("queryString", qs);

        return "client/product/show";
    }

}
