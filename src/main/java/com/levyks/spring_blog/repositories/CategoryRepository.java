package com.levyks.spring_blog.repositories;

import com.levyks.spring_blog.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
