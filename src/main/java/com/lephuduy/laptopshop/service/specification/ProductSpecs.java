package com.lephuduy.laptopshop.service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.Product_;

public class ProductSpecs {
    public static Specification<Product> isLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }

}
