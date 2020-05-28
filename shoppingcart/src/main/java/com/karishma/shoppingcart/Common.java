package com.karishma.shoppingcart;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.karishma.shoppingcart.models.Cart;
import com.karishma.shoppingcart.models.CategoryRepository;
import com.karishma.shoppingcart.models.PageRepository;
import com.karishma.shoppingcart.models.data.Category;
import com.karishma.shoppingcart.models.data.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//this class will intercept other controllers and their methods
//this will be executed first then the other controllers will be executed as needed.
@ControllerAdvice
@SuppressWarnings("unchecked")
public class Common {

    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session){

        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
        List<Category> categories = categoryRepo.findAll();
        boolean cartActive = false;

        if(session.getAttribute("cart") != null){
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");
            int size = 0;
            double total = 0;

            for (Cart value : cart.values()) {
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble((value.getPrice()));
            }

            model.addAttribute("csize", size);
            model.addAttribute("ctotal", total);
            cartActive = true;

        }

        model.addAttribute("ccategories", categories);
        model.addAttribute("cpages", pages);
        model.addAttribute("cartActive", cartActive);

    }


    
}