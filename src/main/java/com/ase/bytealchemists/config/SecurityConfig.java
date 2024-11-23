package com.ase.bytealchemists.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for Password Encoding.
 */
@Configuration
public class SecurityConfig {

  /**
   * Declares BCryptPasswordEncoder as a Spring Bean.
   *
   * @return BCryptPasswordEncoder instance
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the Security Filter Chain to allow unrestricted access to all endpoints.
   *
   * @param http the HttpSecurity object
   * @return the SecurityFilterChain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // Disable CSRF (safe for development, reconsider for production)
        .authorizeRequests()
        .anyRequest().permitAll(); // Allow all requests without authentication

    return http.build();
  }
}

  /**
   * Configures the Security Filter Chain to allow unrestricted access to all endpoints.
   *
   * @param http the HttpSecurity object
   * @return the SecurityFilterChain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable() // Disable CSRF (safe for development, reconsider for production)
            .authorizeRequests()
            .anyRequest().permitAll(); // Allow all requests without authentication

    return http.build();
  }
}

