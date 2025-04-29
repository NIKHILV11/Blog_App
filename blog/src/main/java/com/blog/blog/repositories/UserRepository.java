package com.blog.blog.repositories;
import com.blog.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.foreign.Linker.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Custom query methods can be defined here if needed
    Optional<User> findByEmail(String email); // Find a user by their email address

}
