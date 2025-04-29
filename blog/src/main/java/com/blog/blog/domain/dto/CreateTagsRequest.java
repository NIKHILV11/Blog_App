package com.blog.blog.domain.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagsRequest {

    @NotEmpty(message = "Tag name cannot be empty")
    @Size(min = 10, max = 50, message = "Tag name must be between 2 and 50 characters")
    private Set<
    @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Tag name can only contain letters, numbers, and underscores")
    String> names;
}
