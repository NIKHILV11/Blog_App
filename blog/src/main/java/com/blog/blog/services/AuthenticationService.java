package com.blog.blog.services;

import org.springframework.security.core.userdetails.UserDetails;

import com.blog.blog.domain.dto.LoginRequest;

// import com.blog.blog.domain.entities.User;

public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
    UserDetails registerUser(LoginRequest loginRequest);

}
