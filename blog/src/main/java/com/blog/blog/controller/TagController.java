package com.blog.blog.controller;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blog.domain.dto.TagDto;
import com.blog.blog.domain.entities.Tag;
import com.blog.blog.mappers.TagMapper;
import com.blog.blog.services.TagService;
import com.blog.blog.domain.dto.CreateTagsRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagDto> tagResponses = tags.stream()
                .map(tagMapper::toTagResponse)
                .toList();
        return ResponseEntity.ok(tagResponses);
        // return null;
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagsRequest createTagRequest) {
        List<Tag> savedTags = tagService.createTags(createTagRequest.getNames());
        List<TagDto> createdTagResponses = savedTags.stream()
                .map(tagMapper::toTagResponse)
                .toList();
        return new ResponseEntity<>
                (
                createdTagResponses, 
                HttpStatus.CREATED
                );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();

    }
      

}
