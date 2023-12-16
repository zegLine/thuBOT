/**
 * @file SecurityConfig.java
 * @brief Configuration class for Spring Security
 *
 * This class contains the configuration for Spring Security, defining the security filter chain,
 * user details service, and password encoder
 */
package com.zegline.thubot.core.config;

import com.zegline.thubot.core.service.user.ThuUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @class SecurityConfig
 * @brief Configuration class to set up security filters and user details
 *
 * This class provides beans to set up the security filters, define users with their roles and authorities,
 * and encode their passwords. It uses an in-memory user details manager for simplicity
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable()).headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/database/**").hasRole("SYS")
                        .requestMatchers("/api/input/**").permitAll()
                        .requestMatchers("/api/dialognode/tree/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied") // Specify the custom access denied page
                );
        return http.build();
    }

    /**
     * Configures the password encoder to be used for encoding and matching passwords
     *
     * @return A PasswordEncoder that uses BCrypt hashing for securing passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}