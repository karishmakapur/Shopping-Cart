package com.karishma.shoppingcart.models;

import java.util.List;

import com.karishma.shoppingcart.models.data.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    Category findByName(String name);
    List<Category> findAllByOrderBySortingAsc();
}