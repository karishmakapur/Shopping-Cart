package com.karishma.restclient.controllers;

import java.util.List;

import javax.validation.Valid;

import com.karishma.restclient.models.PageRepository;
import com.karishma.restclient.models.data.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private RestTemplate rest;

    @GetMapping
    public String index(Model model){
        ResponseEntity<List<Page>> response = rest.exchange("http://localhost:8080/pages/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Page>>() {});

        List<Page> pages = response.getBody();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }
    
    @GetMapping("/add")
    public String add(Page page){

        //can also send page by doing below, but it is cleaner
        //to do it through the @ModelAttribute
        //model.addAttribute("page", new Page());
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        //error checking. return to view with error messages
        if (bindingResult.hasErrors()) {
            return "admin/pages/add";
        }

        //success messages
        redirectAttributes.addFlashAttribute("message", "Page added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        //check for slug
        //make page name lower case and replace spaces with dashes if no slug
        //if there is slug, make slug lower case
        //slug is url sensitive 
        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");
        
        //make sure slug doesn't exist
        Page slugExists = pageRepo.findBySlug(slug);

        if ( slugExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);

        } else {
            page.setSlug(slug);
            page.setSorting(100);

            rest.postForObject("http://localhost:8080/admin/pages/add", page, Page.class);
        }

        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Page page = rest.getForObject("http://localhost:8080/admin/pages/edit/{id}", Page.class, id);
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
            rest.put("http://localhost:8080/admin/pages/edit", page);
        }

        return "redirect:/admin/pages/edit/" + page.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes){
        
        rest.delete("http://localhost:8080/admin/pages/delete/{id}", id);
        redirectAttributes.addFlashAttribute("message", "Page deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/pages";
    }
}