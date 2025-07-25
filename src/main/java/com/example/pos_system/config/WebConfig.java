package com.example.pos_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/product-images/**")
                .addResourceLocations("file:///E:/UP_Year_3_S2/Java spring boot/pos_system/uploads/product-images/");
    }


}
