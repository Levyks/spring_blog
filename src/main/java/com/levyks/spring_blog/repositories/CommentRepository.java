package com.levyks.spring_blog.repositories;

import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.models.Post;
import com.levyks.spring_blog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);
   Page<Comment> findByAuthor(User author, Pageable pageable);
}
