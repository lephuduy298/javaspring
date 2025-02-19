package com.lephuduy.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lephuduy.laptopshop.domain.Order;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.repository.OrderDetailRepository;
import com.lephuduy.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
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

    public void deleteAOrderById(long id) {
        // TODO Auto-generated method stub
        this.orderRepository.deleteById(id);
    }

    public void deleteAOrderDetailById(long id) {
        // TODO Auto-generated method stub
        this.orderDetailRepository.deleteById(id);
    }

    public List<Order> getAllByUser(User user) {
        // TODO Auto-generated method stub
        return this.orderRepository.findAllByUser(user);
    }
}
