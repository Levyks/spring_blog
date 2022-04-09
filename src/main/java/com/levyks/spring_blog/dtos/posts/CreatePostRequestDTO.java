package com.levyks.spring_blog.dtos.posts;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreatePostRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
