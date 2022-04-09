package com.levyks.spring_blog.dtos.posts;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateOrUpdatePostRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long categoryId;
}
