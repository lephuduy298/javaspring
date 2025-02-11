package com.lephuduy.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lephuduy.laptopshop.domain.Cart;
import com.lephuduy.laptopshop.domain.CartDetail;
import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.User;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    // void save(CartDetail cartDetail);

    CartDetail findByCartAndProduct(Cart cart, Product product);

    boolean existsByCartAndProduct(Cart cart, Product product);

    List<CartDetail> findByCart(Cart cart);

}
