package com.sales_scout.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://161.97.149.132")
                .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
        ;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/contracts/**")
                .addResourceLocations("file:src/main/resources/contracts/");

        registry.addResourceHandler("/files/interactions/**")
                .addResourceLocations("file:src/main/resources/interactions/");
    }
}
