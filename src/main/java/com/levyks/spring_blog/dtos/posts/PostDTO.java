package com.levyks.spring_blog.dtos.posts;

import com.levyks.spring_blog.dtos.auth.UserBasicDTO;
import com.levyks.spring_blog.dtos.categories.BasicCategoryDTO;
import com.levyks.spring_blog.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Boolean edited;
    private Date createdAt;
    private Date updatedAt;
    private UserBasicDTO author;
    private BasicCategoryDTO category;

    public static PostDTO fromPost(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getEdited(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                UserBasicDTO.fromUser(post.getAuthor()),
                BasicCategoryDTO.fromCategory(post.getCategory())
        );
    }
}
