package com.karishma.shoppingcart.models;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.karishma.shoppingcart.models.data.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
    Page findBySlug(String slug);

    //custom query
    /*
    @Query("SELECT p from Page p WHERE p.id != :id and p.slug = :slug")
    Page findBySlug(int id, String slug);
    */

    Page findBySlugAndIdNot(String slug, int id);

    List<Page> findAllByOrderBySortingAsc();

}