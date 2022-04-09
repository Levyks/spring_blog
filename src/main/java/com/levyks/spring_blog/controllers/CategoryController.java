package com.levyks.spring_blog.controllers;

import com.levyks.spring_blog.dtos.categories.CategoryDTO;
import com.levyks.spring_blog.dtos.categories.CreateOrUpdateCategoryRequestDTO;
import com.levyks.spring_blog.dtos.posts.PostDTO;
import com.levyks.spring_blog.models.Category;
import com.levyks.spring_blog.repositories.CategoryRepository;
import com.levyks.spring_blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("")
    public Page<CategoryDTO> getCategories(
            @RequestParam(value="q", required = false) String query,
            Pageable pageable
    ) {
        return query == null ?
                categoryRepository.findAll(pageable).map(CategoryDTO::fromCategory) :
                categoryRepository.findByNameContainingIgnoreCase(query, pageable).map(CategoryDTO::fromCategory);
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return CategoryDTO.fromCategory(category);
    }

    @GetMapping("/{id}/posts")
    public Page<PostDTO> getPostsByCategory(
            @PathVariable Long id,
            Pageable pageable
    ) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return postRepository.findByCategory(category, pageable).map(PostDTO::fromPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateCategoryRequestDTO categoryDTO
            ) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(categoryDTO.getName());

        return CategoryDTO.fromCategory(categoryRepository.save(category));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO createCategory(@RequestBody @Valid CreateOrUpdateCategoryRequestDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        return CategoryDTO.fromCategory(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }
}

