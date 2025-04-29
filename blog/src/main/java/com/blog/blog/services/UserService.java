package com.blog.blog.services;

import java.util.UUID;

import com.blog.blog.domain.entities.User;

public interface UserService {
    User getUserById(UUID id);
} 
