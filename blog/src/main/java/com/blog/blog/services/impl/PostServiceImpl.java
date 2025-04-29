package com.blog.blog.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.blog.blog.domain.CreatePostRequest;
import com.blog.blog.domain.PostStatus;
import com.blog.blog.domain.UpdatePostRequest;
import com.blog.blog.domain.entities.Category;
import com.blog.blog.domain.entities.Post;
import com.blog.blog.domain.entities.Tag;
import com.blog.blog.domain.entities.User;
import com.blog.blog.repositories.PostRepository;
import com.blog.blog.services.CategoryService;
import com.blog.blog.services.PostService;
import com.blog.blog.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final TagService tagService;
    private final CategoryService categoryService;

    @Override
    @Transactional 
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        } else if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        } else if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED,tag);
        } else {
            return postRepository.findAllByStatus(PostStatus.PUBLISHED);
        }
       
    }
    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);
    }
    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        post.setStatus(createPostRequest.getStatus());
        post.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        post.setAuthor(user);

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        post.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagsByIds(tagIds);
        post.setTags(new HashSet<>(tags)); // Convert Set to List and assign to post.tags);

        
        return postRepository.save(post);
    }
    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0; // No content, no reading time
        }
        // Assuming an average reading speed of 200 words per minute
        int words = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) words / 200);
    }
    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Post not found with id: " + id));
        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());
        post.setStatus(updatePostRequest.getStatus());
        post.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));
        
        Category category = categoryService.getCategoryById(updatePostRequest.getCategoryId());
        post.setCategory(category);

        Set<UUID> tagIds = updatePostRequest.getTagIds();
        List<Tag> tags = tagService.getTagsByIds(tagIds);
        post.setTags(new HashSet<>(tags)); // Convert Set to List and assign to post.tags);
        return postRepository.save(post);
        
    }
    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
    }

    @Override
    public void deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        postRepository.delete(post);
    }
}
