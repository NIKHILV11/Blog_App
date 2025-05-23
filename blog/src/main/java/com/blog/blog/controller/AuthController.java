package com.blog.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blog.domain.dto.AuthResponse;
import com.blog.blog.domain.dto.LoginRequest;
import com.blog.blog.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth/login")
// @CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login request: " );
        UserDetails userDetails;
    try {
        userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
    } catch (Exception e) {
        // Register new user if not found or bad credentials
        userDetails = authenticationService.registerUser(loginRequest);
    }

    System.out.println("User details:");
    String tokenValue = authenticationService.generateToken(userDetails);
    AuthResponse authResponse = AuthResponse.builder()
            .token(tokenValue)
            .expiresIn(86400)
            .build();
    return ResponseEntity.ok(authResponse);
    }
    

}
