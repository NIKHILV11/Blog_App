package com.blog.blog.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.blog.blog.domain.entities.User;
import com.blog.blog.repositories.UserRepository;
import com.blog.blog.services.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

}
