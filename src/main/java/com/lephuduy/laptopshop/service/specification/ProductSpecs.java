package com.lephuduy.laptopshop.service.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.lephuduy.laptopshop.domain.Product;
import com.lephuduy.laptopshop.domain.Product_;

public class ProductSpecs {
    public static Specification<Product> isLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }

    public static Specification<Product> isMinPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get(Product_.PRICE), minPrice);
    }

    public static Specification<Product> isMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get(Product_.PRICE), maxPrice);
    }

    public static Specification<Product> isFactory(String factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.FACTORY), "%" + factory + "%");
    }

    public static Specification<Product> isListFactory(List<String> factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factory);
    }

    public static Specification<Product> isListTarget(List<String> target) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.TARGET)).value(target);
    }

    public static Specification<Product> isPrice(Double min, Double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.ge(root.get(Product_.PRICE), min),
                criteriaBuilder.le(root.get(Product_.PRICE), max));
    }

    public static Specification<Product> matchMultilePrice(Double min, Double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                root.get(Product_.PRICE), min, max);

    }

}
