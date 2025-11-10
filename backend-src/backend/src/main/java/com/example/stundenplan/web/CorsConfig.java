package com.example.stundenplan.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins("http://localhost:19006","http://localhost:3000","*")
      .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
      .allowedHeaders("*");
  }
}

