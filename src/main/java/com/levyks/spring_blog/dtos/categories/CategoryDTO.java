package com.levyks.spring_blog.dtos.categories;

import com.levyks.spring_blog.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private int postCount;

    public static CategoryDTO fromCategory(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getPostCount()
        );
    }
}
