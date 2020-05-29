package com.karishma.shoppingcart.models;

import com.karishma.shoppingcart.models.data.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}