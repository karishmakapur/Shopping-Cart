package com.karishma.shoppingcart.controllers;

import java.util.List;

import javax.validation.Valid;

import com.karishma.shoppingcart.models.CategoryRepository;
import com.karishma.shoppingcart.models.data.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
    }
    
    @GetMapping("/add")
    //can send a different name to html file by saying @ModelAttribute("name")
    //instead of referencing category, you need to reference name, as that is the
    //title passed
    public String add(@ModelAttribute Category category){

        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){


         //error checking. return to view with error messages
         if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }

        //success messages
        redirectAttributes.addFlashAttribute("message", "Category added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        //check for slug
        //make page name lower case and replace spaces with dashes if no slug
        //if there is slug, make slug lower case
        //slug is url sensitive 
        String slug = category.getName().toLowerCase().replace(" ", "-");
        
        //make sure slug doesn't exist
        Category slugExists = categoryRepo.findByName(category.getName());

        if ( slugExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("categoryInfo", category);

        } else {
            category.setSlug(slug);
            category.setSorting(100);

            categoryRepo.save(category);
        }

        return "redirect:/admin/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){

        Category category = categoryRepo.getOne(id);
        model.addAttribute("category", category);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Category categoryCurent = categoryRepo.getOne(category.getId());

        //error checking. return to view with error messages
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", categoryCurent.getName());
            return "admin/categories/edit";
        }

        //success messages
        redirectAttributes.addFlashAttribute("message", "Category edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        //check for slug
        //make page name lower case and replace spaces with dashes if no slug
        //if there is slug, make slug lower case
        //slug is url sensitive 
        String slug = category.getName().toLowerCase().replace(" ", "-");
        
        //make sure slug doesn't exist (it can be the current page, but not another)
        Category slugExists = categoryRepo.findByName(category.getName());

        if ( slugExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("category", category);

        } else {
            category.setSlug(slug);
            categoryRepo.save(category);
        }

        return "redirect:/admin/categories/edit/" + category.getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes){
        
        categoryRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Category deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id){

        int count = 1;
        Category category;

        for(int pageId : id){
            category = categoryRepo.getOne(pageId);
            category.setSorting(count);
            categoryRepo.save(category);
            count++;
        }

        return "ok";
    }

}