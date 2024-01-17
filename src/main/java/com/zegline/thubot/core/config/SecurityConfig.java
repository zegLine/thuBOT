/**
 * @file SecurityConfig.java
 * @brief Holds the configuration class for security purposes in the Spring application.
 *
 * This file contains the SecurityConfig class which provides the security configurations of the application.
 * It configures the security filter chain, user details service, and password encoder.
 */
package com.zegline.thubot.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * @class SecurityConfig
 * @brief Configures the security settings including security filters, user details, and password encoding.
 *
 * This class defines the beans for setting up the security filters, defines users with their roles and authorities,
 * and encodes their passwords by configuring the PasswordEncoder. The user details are managed in-memory for simplicity.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain to set up CORS, CSRF, and endpoint access rules
     *
     * @param http HttpSecurity to configure the request authorizations and access rules
     * @return The configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.addHeaderWriter(new StaticHeadersWriter("X-Frame-Options", "SAMEORIGIN")))
            .authorizeHttpRequests(authz -> {
                authz.requestMatchers("/database/**").hasRole("SYS");
                authz.requestMatchers("/api/input/**").permitAll();
                authz.requestMatchers("/api/dialognode/get").permitAll();
                authz.requestMatchers("/api/**").authenticated();
                authz.anyRequest().permitAll();
            })
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/access-denied")
            );
        return http.build();
    }

    /**
     * Configures the password encoder to be used for encoding and matching passwords
     *
     * @return A PasswordEncoder that uses BCrypt hashing for securing passwords
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}