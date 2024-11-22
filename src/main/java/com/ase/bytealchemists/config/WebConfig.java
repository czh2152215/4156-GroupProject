package com.ase.bytealchemists.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global configuration to allow unrestricted CORS for all endpoints.
 */
@Configuration
public class WebConfig {

  /**
    * Creates a {@link WebMvcConfigurer} bean that defines global CORS settings.
    */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
            .allowedOrigins("*") // Allow all origins
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow common HTTP methods
            .allowedHeaders("*") // Allow all headers
            .exposedHeaders("Authorization", "Content-Type") // Expose specific headers if needed
            .allowCredentials(false); // Disallow credentials if using wildcard origins
      }
    };
  }
}
