package com.karishma.shoppingcart;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Indicates to spring that this is a configuration class 
//that will provide beans to application context
@Configuration
public class WebConfig implements WebMvcConfigurer{

    //better way to return a view rather than the @GetMapping annotation in a controller
   /* @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       registry.addViewController("/").setViewName("home");
    }*/

    

   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/media/**").addResourceLocations("file:///C:/Users/kishu/OneDrive/vs_code_workspace/shopping-cart/shoppingcart/src/main/resources/static/media/");
   }

}