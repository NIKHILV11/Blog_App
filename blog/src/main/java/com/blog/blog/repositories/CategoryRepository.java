package com.blog.blog.repositories;
import com.blog.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // pu
    // Custom query methods can be defined here if needed
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount(); // This method is already provided by JpaRepository, so you may not need to define it again.

    boolean existsByNameIgnoreCase(String name); // Check if a category with the same name already exists


}
