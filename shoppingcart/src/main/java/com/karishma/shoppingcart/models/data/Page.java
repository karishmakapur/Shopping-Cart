package com.karishma.shoppingcart.models.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="pages")
@Data
public class Page {

    //columns in table in database

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //primary key

    private String title;
    private String slug;
    private String content;
    private int sorting;
    
}