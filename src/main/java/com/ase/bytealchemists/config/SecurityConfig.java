package com.ase.bytealchemists.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
}


