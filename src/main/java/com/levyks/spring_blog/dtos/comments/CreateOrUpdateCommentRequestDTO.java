package com.levyks.spring_blog.dtos.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCommentRequestDTO {
    @NotBlank
    private String content;
}
