package com.lephuduy.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(long id) {
        return this.userRepository.findUserById(id);
    }

    public List<User> getAllByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User handleSaveUser(User user) {
        User duy = this.userRepository.save(user);
        System.out.println(duy);
        return duy;
    }
}
