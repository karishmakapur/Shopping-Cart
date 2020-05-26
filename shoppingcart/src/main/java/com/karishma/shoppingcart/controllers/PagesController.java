package com.karishma.shoppingcart.controllers;

import com.karishma.shoppingcart.models.PageRepository;
import com.karishma.shoppingcart.models.data.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PagesController {

    @Autowired
    private PageRepository pageRepo;

    @GetMapping
    public String home(Model model){
        Page page = pageRepo.findBySlug("home");
        model.addAttribute("page", page);
        return "page";
    }
    
}