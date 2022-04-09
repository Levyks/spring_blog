package com.levyks.spring_blog.dtos.misc;

import com.levyks.spring_blog.dtos.comments.CommentDTO;
import com.levyks.spring_blog.dtos.posts.PostDTO;
import com.levyks.spring_blog.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivtyDTO {
    private ActivityType type;
    private PostDTO post;
    private CommentDTO comment;
}
