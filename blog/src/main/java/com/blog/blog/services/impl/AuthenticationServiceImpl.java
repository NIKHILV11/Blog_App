package com.blog.blog.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.blog.domain.dto.LoginRequest;
import com.blog.blog.domain.entities.User;
import com.blog.blog.repositories.UserRepository;
// import com.blog.blog.domain.entities.User;
import com.blog.blog.services.AuthenticationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;
    private final Long expirationTime = 3600000L; // 1 hour in milliseconds
    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        System.out.println("Authentication successful for email: " + email);
        return userDetailsService.loadUserByUsername(email);
       
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
        
    }
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        
            return claims.getSubject();}
     @Override
public UserDetails registerUser(LoginRequest loginRequest) {
    if (userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
        throw new RuntimeException("Invalid password");
    }

    User newUser = User.builder()
            .name("New User") // or make LoginRequest include name field
            .email(loginRequest.getEmail())
            .password(passwordEncoder.encode(loginRequest.getPassword()))
            .build();

    userRepository.save(newUser);
    return userDetailsService.loadUserByUsername(newUser.getEmail());
    }

}
