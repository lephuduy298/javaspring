package com.lephuduy.laptopshop.service;

import org.springframework.stereotype.Service;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        User duy = this.userRepository.save(user);
        System.out.println(duy);
        return duy;
    }
}
