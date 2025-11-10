package com.example.stundenplan.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable()) 
      .authorizeHttpRequests(auth -> auth
        // Swagger + docs
        .requestMatchers(
          "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        // open for all get
        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
        // POST only ADMIN
        .requestMatchers(HttpMethod.POST, "/api/v1/unterricht**").hasRole("ADMIN")
        // for every other things
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/unterricht**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/v1/unterricht**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/v1/unterricht**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .httpBasic(Customizer.withDefaults()); // test basic auth
    return http.build();
  }

  @Bean
  UserDetailsService users(PasswordEncoder pe) {
    UserDetails admin = User.withUsername("admin").password(pe.encode("admin")).roles("ADMIN").build();
    UserDetails lehrer = User.withUsername("lehrer").password(pe.encode("lehrer")).roles("LEHRER").build();
    UserDetails schueler = User.withUsername("schueler").password(pe.encode("schueler")).roles("SCHUELER").build();
    return new InMemoryUserDetailsManager(admin, lehrer, schueler);
  }

  @Bean
  PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}

