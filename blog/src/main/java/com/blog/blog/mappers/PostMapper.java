package com.blog.blog.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.blog.blog.domain.CreatePostRequest;
import com.blog.blog.domain.UpdatePostRequest;
import com.blog.blog.domain.dto.CreatePostRequestDto;
import com.blog.blog.domain.dto.PostDto;
import com.blog.blog.domain.dto.UpdatePostRequestDto;
import com.blog.blog.domain.entities.Post;
@Mapper(componentModel = "spring",unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author",source = "author")
    @Mapping(target = "category",source = "category")
    @Mapping(target = "tags",source = "tags")
    @Mapping(target = "status",source = "status")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto postDto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto postDto);

}
