package com.blog.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.blog.blog.domain.PostStatus;
import com.blog.blog.domain.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private UUID id;
    private String title;
    private String content;
    private AuthorDto author;
    private CategoryDto category;
    private Integer readingTime;
    private Set<TagDto> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostStatus status;

}
