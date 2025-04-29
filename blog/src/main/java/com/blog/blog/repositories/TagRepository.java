package com.blog.blog.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.blog.blog.domain.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    // Custom query methods can be defined here if needed
    @Query("SELECT t from Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllWithPostCount(); // Fetch all tags with their associated posts

    List<Tag> findByNameIn(Set<String> tagNames); // Fetch tags by their names
}
