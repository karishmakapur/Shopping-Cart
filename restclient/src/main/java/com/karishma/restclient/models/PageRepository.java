package com.karishma.restclient.models;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.karishma.restclient.models.data.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
    Page findBySlug(String slug);

    Page findBySlugAndIdNot(String slug, int id);

    List<Page> findAllByOrderBySortingAsc();

}