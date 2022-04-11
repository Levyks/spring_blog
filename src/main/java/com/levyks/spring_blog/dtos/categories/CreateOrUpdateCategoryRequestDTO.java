package com.levyks.spring_blog.dtos.categories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCategoryRequestDTO {
    @NotBlank
    private String name;
}
