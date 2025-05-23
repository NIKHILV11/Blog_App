package com.blog.blog.services;

import java.util.List;
import java.util.UUID;

import com.blog.blog.domain.CreatePostRequest;
import com.blog.blog.domain.UpdatePostRequest;
import com.blog.blog.domain.entities.Post;
import com.blog.blog.domain.entities.User;

public interface PostService {

    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id,UpdatePostRequest updatePostRequest);
    Post getPost(UUID id);
    void deletePost(UUID id);


}
