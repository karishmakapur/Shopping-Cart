package com.karishma.shoppingcart.models;

import com.karishma.shoppingcart.models.data.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    
    Product findBySlug(String slug);

    Product findBySlugAndIdNot(String slug, int id);
    
    Page<Product> findAll(Pageable pageable);
}