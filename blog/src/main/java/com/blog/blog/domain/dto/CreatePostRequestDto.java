package com.blog.blog.domain.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.blog.blog.domain.PostStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequestDto {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 10000, message = "Content must be less than 10000 characters")
    private String content;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @Builder.Default
    @Size(min = 1, message = "At least one tag is required")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "status is required")
    private PostStatus status;


}
