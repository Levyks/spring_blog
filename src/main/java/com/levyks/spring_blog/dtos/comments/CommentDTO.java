package com.levyks.spring_blog.dtos.comments;

import com.levyks.spring_blog.dtos.auth.BasicUserDTO;
import com.levyks.spring_blog.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Boolean edited;
    private Date createdAt;
    private Date updatedAt;
    private Long postId;
    private BasicUserDTO author;

    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getEdited(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getPost().getId(),
                BasicUserDTO.fromUser(comment.getAuthor())
        );
    }
}

