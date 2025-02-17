package com.lephuduy.laptopshop.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lephuduy.laptopshop.domain.Cart;
import com.lephuduy.laptopshop.domain.CartDetail;
import com.lephuduy.laptopshop.domain.Order;
import com.lephuduy.laptopshop.domain.OrderDetail;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.service.OrderService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getDashBoard(Model model) {
        List<Order> orders = this.orderService.findAllCart();
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String createUserInfo(Model model, @PathVariable long id) {
        Optional<Order> optionalOrder = this.orderService.findOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            List<OrderDetail> orderDetails = order == null ? new ArrayList<OrderDetail>() : order.getOrderDetails();
            int sum = 0;
            for (OrderDetail orderDetail : orderDetails) {
                sum += orderDetail.getPrice();
            }
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("totalPrice", sum);

        }
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdatePage(Model model, @PathVariable long id) {
        Optional<Order> optionalOrder = this.orderService.findOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            model.addAttribute("newOrder", order);
            // model.addAttribute("totalPrice", sum);

        }
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String postUpdateOrder(@ModelAttribute("newOrder") Order order) {
        // TODO: process POST request
        Optional<Order> optionalOrder = this.orderService.findOrderById(order.getId());
        if (optionalOrder != null) {
            Order currentOrder = optionalOrder.get();
            currentOrder.setStatus(order.getStatus());

            this.orderService.save(currentOrder);
        }
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeletePage(Model model, @PathVariable long id) {
        Optional<Order> optionalOrder = this.orderService.findOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            model.addAttribute("newOrder", order);
        }
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteOrder(@ModelAttribute("newOrder") Order order) {
        // TODO: process POST request
        Optional<Order> optionalOrder = this.orderService.findOrderById(order.getId());
        if (optionalOrder != null) {
            Order currentOrder = optionalOrder.get();
            List<OrderDetail> orderDetails = currentOrder.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                this.orderService.deleteAOrderDetailById(orderDetail.getId());
            }
            this.orderService.deleteAOrderById(currentOrder.getId());
        }
        return "redirect:/admin/order";
    }

}
