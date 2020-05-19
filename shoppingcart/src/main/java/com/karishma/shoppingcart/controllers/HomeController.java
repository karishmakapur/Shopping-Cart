package com.karishma.shoppingcart.controllers;

import org.springframework.stereotype.Controller; //import for controller class
import org.springframework.web.bind.annotation.GetMapping;

//Handles requests
//Home Controller Class

//annotation to make it a controller class
@Controller 
public class HomeController {

    //handles request from root domain
    //get is the request.
    //method-level annotation
    //returns a view
    @GetMapping("/someRandomPage")
    public String home(){
        return "home"; //home view file that is inside templates folder
    }
    
}