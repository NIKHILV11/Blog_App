package com.blog.blog.mappers;
import java.util.List;
import com.blog.blog.domain.entities.Category;
import org.mapstruct.*;
import org.springframework.security.access.method.P;

import com.blog.blog.domain.PostStatus;
import com.blog.blog.domain.entities.Post;
import com.blog.blog.domain.dto.CategoryDto;
import com.blog.blog.domain.dto.CreateCategoryRequest;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount",source = "posts",qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);
    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if(posts == null) {
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }


}
