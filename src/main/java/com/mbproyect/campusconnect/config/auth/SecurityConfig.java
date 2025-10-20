package com.mbproyect.campusconnect.config.auth;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Modifies the security filters
     * Disables the csrf in order to make a stateless request server
     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login/**", "/oauth2/**", "/logout").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(customAuthSuccessHandler()) // filtro personalizado
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("https://login.microsoftonline.com/common/oauth2/v2.0/logout?post_logout_redirect_uri=http://localhost:8081")
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                )
//                .build();
//    }
//
//    @Bean
//    public CustomAuthSuccessHandler customAuthSuccessHandler() {
//        return new CustomAuthSuccessHandler();
//    }


}
