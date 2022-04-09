package com.levyks.spring_blog.repositories;

import com.levyks.spring_blog.models.Category;
import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.models.Post;
import com.levyks.spring_blog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCategory(Category category, Pageable pageable);
    Page<Post> findByAuthor(User author, Pageable pageable);
}
