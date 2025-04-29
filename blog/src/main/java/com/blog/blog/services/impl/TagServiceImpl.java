package com.blog.blog.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;

import com.blog.blog.repositories.TagRepository;
import org.springframework.stereotype.Service;

import com.blog.blog.domain.entities.Tag;
import com.blog.blog.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }
    @Override
    @Transactional
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        List<Tag> newTags = tagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .map(name -> Tag.builder()
                         .name(name)
                         .posts(new HashSet<>())
                         .build()
                    )
                .toList();

        List<Tag> savedTages = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTages = tagRepository.saveAll(newTags);
        }
        savedTages.addAll(existingTags);
       
                
        return savedTages;
    }
    @Override
    @Transactional
    public void deleteTag(UUID tagId) {
        tagRepository.findById(tagId)
                .ifPresent(tag -> {
                    if(!tag.getPosts().isEmpty()) {
                        throw new IllegalStateException("Cannot delete tag with associated posts.");
                    } 
                    tagRepository.deleteById(tagId);
                    
                });
    }
    @Override
    public Tag getTagById(UUID tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
    }
    @Override
    public List<Tag> getTagsByIds(Set<UUID> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if(tags.size() != tagIds.size())
        {
            throw new EntityNotFoundException("Some tags not found with ids: " + tagIds);
        }
        return tags;
    }
}
