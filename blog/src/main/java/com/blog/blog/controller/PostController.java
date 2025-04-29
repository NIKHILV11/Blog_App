package com.blog.blog.controller;

import java.util.List;
import java.util.UUID;

import com.blog.blog.domain.CreatePostRequest;
import com.blog.blog.domain.UpdatePostRequest;
import com.blog.blog.domain.dto.CreatePostRequestDto;
import com.blog.blog.domain.dto.PostDto;
import com.blog.blog.domain.dto.UpdatePostRequestDto;
import com.blog.blog.domain.entities.Post;
import com.blog.blog.domain.entities.User;
import com.blog.blog.mappers.PostMapper;
import com.blog.blog.services.PostService;
import com.blog.blog.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostMapper postMapper;
    private final PostService postService; 
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
        @RequestParam(required = false) UUID categoryId,
        @RequestParam(required = false) UUID tagId
    ) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream()
                .map(postMapper :: toDto)
                .toList();
        return ResponseEntity.ok(postDtos);
    }
    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        List<Post> drafts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = drafts.stream()
                .map(postMapper :: toDto)
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
        @Valid @RequestBody CreatePostRequestDto createPostRequestDto, 
        @RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(loggedInUser, createPostRequest);
        PostDto postDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(postDto,HttpStatus.CREATED);

    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
        @PathVariable UUID id, 
        @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto postDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(postDto);
        
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
