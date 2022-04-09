package com.levyks.spring_blog.dtos.categories;

import com.levyks.spring_blog.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicCategoryDTO {
    private Long id;
    private String name;

    public static BasicCategoryDTO fromCategory(Category category) {
        return new BasicCategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
