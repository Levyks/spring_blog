package com.levyks.spring_blog.repositories;

import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Iterable<Comment> findByPost(Post post);
    Iterable<Comment> findByPostId(Long id);
    Page<Comment> findByPostId(Long id, Pageable pageable);
}
