package com.karishma.shoppingcart.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;

import javax.validation.Valid;

import com.karishma.shoppingcart.models.CategoryRepository;
import com.karishma.shoppingcart.models.ProductRepository;
import com.karishma.shoppingcart.models.data.Category;
import com.karishma.shoppingcart.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model) {
        List<Product> products = productRepo.findAll();
        List<Category> categories = categoryRepo.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Category category : categories) {
            cats.put(category.getId(), category.getName());
        }

        model.addAttribute("products", products);
        model.addAttribute("cats", cats);
        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file,
            RedirectAttributes redirectAttributes, Model model) throws IOException {

        List<Category> categories = categoryRepo.findAll();

        //error checking. return to view with error messages
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "admin/products/add";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if(filename.endsWith("jpg") || filename.endsWith("png")){
            fileOK = true;
        }

        //success messages
        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        //check for slug
        //make page name lower case and replace spaces with dashes if no slug
        //if there is slug, make slug lower case
        //slug is url sensitive 
        String slug = product.getName().toLowerCase().replace(" ", "-");
        
        //make sure slug doesn't exist
        Product slugExists = productRepo.findBySlug(slug);

        if(!fileOK){
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);    
        }
        else if ( slugExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);

        } else {
            product.setSlug(slug);
            product.setImage(filename);
            productRepo.save(product);

            Files.write(path,bytes);
        }

        return "redirect:/admin/products/add";
    }
    /*
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Page page = pageRepo.getOne(id);
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Page pageCurrent = pageRepo.getOne(page.getId());

        //error checking. return to view with error messages
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", pageCurrent.getTitle());
            return "admin/pages/edit";
        }

        //success messages
        redirectAttributes.addFlashAttribute("message", "Page edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        //check for slug
        //make page name lower case and replace spaces with dashes if no slug
        //if there is slug, make slug lower case
        //slug is url sensitive 
        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");
        
        //make sure slug doesn't exist (it can be the current page, but not another)
        Page slugExists = pageRepo.findBySlugAndIdNot(slug, page.getId());

        if ( slugExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);

        } else {
            page.setSlug(slug);
            pageRepo.save(page);
        }

        return "redirect:/admin/pages/edit/" + page.getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes){
        
        pageRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Page deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/pages";
    }*/
}