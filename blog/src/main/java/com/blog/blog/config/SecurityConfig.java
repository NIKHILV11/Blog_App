package com.blog.blog.config;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.blog.blog.domain.entities.User;
import com.blog.blog.repositories.UserRepository;
import com.blog.blog.security.BlogUserDetailsService;
import com.blog.blog.security.JwtAuthenticationFilter;
import com.blog.blog.services.AuthenticationService;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(userRepository);

        String email = "hellnix.47@gmail.com";
        userRepository.findByEmail(email).orElseGet(() -> {
            User user = User.builder()
            .name("User")
            .email(email)
            .password(passwordEncoder().encode("password"))
            .build();
           
            return userRepository.save(user);
        });
        return blogUserDetailsService;
        

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/auth/drafts").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/tags/**").permitAll()
                .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity, not recommended for production
        .sessionManagement(
            session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions for REST APIs
        ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before the authentication filter
        return http.build();
        
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();}
}
