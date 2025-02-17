package com.lephuduy.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lephuduy.laptopshop.domain.Role;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.domain.dto.RegisterDTO;
import com.lephuduy.laptopshop.repository.OrderRepository;
import com.lephuduy.laptopshop.repository.ProductRepository;
import com.lephuduy.laptopshop.repository.RoleRepository;
import com.lephuduy.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(long id) {
        return this.userRepository.findUserById(id);
    }

    public void deleteAUser(long id) {
        this.userRepository.deleteById(id);
    }

    public List<User> getAllByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User handleSaveUser(User user) {
        User duy = this.userRepository.save(user);
        System.out.println(duy);
        return duy;
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public User registerOTDtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public long countUsers() {
        return this.userRepository.count();
    }

    public long countOrders() {
        return this.orderRepository.count();
    }

    public long countProducts() {
        return this.productRepository.count();
    }
}
