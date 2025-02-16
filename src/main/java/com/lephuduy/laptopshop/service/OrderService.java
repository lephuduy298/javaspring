package com.lephuduy.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lephuduy.laptopshop.domain.Order;
import com.lephuduy.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllCart() {
        return this.orderRepository.findAll();
    }

    public Optional<Order> findOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public void save(Order order) {
        // TODO Auto-generated method stub
        this.orderRepository.save(order);
    }
}
