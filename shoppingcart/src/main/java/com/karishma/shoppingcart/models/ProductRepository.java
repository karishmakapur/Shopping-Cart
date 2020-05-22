package com.karishma.shoppingcart.models;

import com.karishma.shoppingcart.models.data.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    
    Product findBySlug(String slug);
}