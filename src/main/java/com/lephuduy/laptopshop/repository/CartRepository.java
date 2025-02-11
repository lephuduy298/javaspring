package com.lephuduy.laptopshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lephuduy.laptopshop.domain.Cart;
import com.lephuduy.laptopshop.domain.CartDetail;
import com.lephuduy.laptopshop.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);

}
