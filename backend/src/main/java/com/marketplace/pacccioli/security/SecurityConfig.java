package com.marketplace.pacccioli.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
 
    @Autowired
    private JwtFilter jwtFilter;

    public SecurityConfig() {
        System.out.println("=== SecurityConfig cargado ===");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
    .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/uploads/**").permitAll()
            .requestMatchers("/api/**").permitAll()
            .anyRequest().authenticated()
    )
        .httpBasic(httpBasic -> httpBasic.disable())
        .formLogin(form -> form.disable())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}