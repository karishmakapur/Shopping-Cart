package com.karishma.restapi.models;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import com.karishma.restapi.models.data.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
    Optional<Page> findBySlug(String slug);

    Page findBySlugAndIdNot(String slug, int id);

    List<Page> findAllByOrderBySortingAsc();

}