package com.karishma.shoppingcart.models;

import org.springframework.data.jpa.repository.JpaRepository;
import com.karishma.shoppingcart.models.data.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {

}