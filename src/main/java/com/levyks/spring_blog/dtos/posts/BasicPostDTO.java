package com.levyks.spring_blog.dtos.posts;

import com.levyks.spring_blog.dtos.auth.BasicUserDTO;
import com.levyks.spring_blog.dtos.categories.BasicCategoryDTO;
import com.levyks.spring_blog.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BasicPostDTO {
    private Long id;
    private String title;
    private String contentShort;
    private Date createdAt;
    private String author;
    private String category;

    public static BasicPostDTO fromPost(Post post) {
        return new BasicPostDTO(
                post.getId(),
                post.getTitle(),
                post.getContentShort(true),
                post.getCreatedAt(),
                post.getAuthor().getFullname(),
                post.getCategory().getName()
        );
    }
}
