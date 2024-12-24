package com.lephuduy.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.User;
import com.lephuduy.laptopshop.repository.ProductRepository;
import com.lephuduy.laptopshop.repository.RoleRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;

    public ProductService(ProductRepository productRepository, RoleRepository roleRepository) {
        this.productRepository = productRepository;
        this.roleRepository = roleRepository;
    }

    public Product handleSaveProduct(Product product) {
        Product duy = this.productRepository.save(product);
        System.out.println(duy);
        return duy;
    }

    public List<Product> fecthProducts() {
        return this.productRepository.findAll();
    }
}
