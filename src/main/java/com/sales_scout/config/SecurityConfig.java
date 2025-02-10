package com.sales_scout.config;

//package com.sales_scout.config;

//import com.sales_scout.Auth.JWTAuthenticationEntryPoint;
//import com.sales_scout.Auth.JwtAuthenticationFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import com.sales_scout.Auth.JWTAuthenticationEntryPoint;
//import com.sales_scout.Auth.JwtAuthenticationFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
//                .csrf().disable(); // Disable CSRF for simplicity
//        return http.build();
//    }
//}




import com.sales_scout.Auth.JWTAuthenticationEntryPoint;
import com.sales_scout.Auth.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailService,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/home", "/api/leads/dashboard").permitAll()
                        .requestMatchers("/api/images/**").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/create").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) -> handleException(request, response, 403))
                        .defaultAuthenticationEntryPointFor(
                                (request, response, e) -> handleException(request, response, 500),
                                request -> true // Default handler for any exception
                        )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, int status) throws IOException, IOException {
        Object errorCode = request.getAttribute("javax.servlet.error.status_code");

        if (errorCode != null && (int) errorCode == 404) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Resource Not Found");
        } else if (status == 403) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied");
        } else if (status == 500) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Unexpected Error");
        }
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}