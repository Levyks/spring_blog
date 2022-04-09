package com.levyks.spring_blog.dtos.categories;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateOrUpdateCategoryRequestDTO {
    private String title; // For some reason it breaks if I remove this ???
    @NotBlank
    private String name;
}
