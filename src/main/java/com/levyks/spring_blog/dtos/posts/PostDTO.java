package com.levyks.spring_blog.dtos.posts;

import com.levyks.spring_blog.dtos.auth.UserBasicDTO;
import com.levyks.spring_blog.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    public static PostDTO fromPost(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getEdited(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                UserBasicDTO.fromUser(post.getAuthor())
        );
    }
}
