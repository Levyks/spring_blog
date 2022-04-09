package com.levyks.spring_blog.controllers;

import com.levyks.spring_blog.dtos.auth.UserBasicDTO;
import com.levyks.spring_blog.dtos.comments.CommentDTO;
import com.levyks.spring_blog.dtos.posts.PostDTO;
import com.levyks.spring_blog.models.User;
import com.levyks.spring_blog.repositories.CommentRepository;
import com.levyks.spring_blog.repositories.PostRepository;
import com.levyks.spring_blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public UserController(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("")
    public Page<UserBasicDTO> getUsers(
            @RequestParam(value="q", required = false) String query,
            Pageable pageable
    ) {
        return query == null ?
                userRepository.findAll(pageable).map(UserBasicDTO::fromUser) :
                userRepository.searchByName(query, pageable).map(UserBasicDTO::fromUser);
    }

    @GetMapping("/{id}")
    public UserBasicDTO getUser(@PathVariable Long id) {
        return userRepository.findById(id).map(UserBasicDTO::fromUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/{id}/posts")
    public Page<PostDTO> getPostsByUser(@PathVariable Long id, Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return postRepository.findByAuthor(user, pageable).map(PostDTO::fromPost);
    }

    @GetMapping("/{id}/comments")
    public Page<CommentDTO> getCommentsByUser(@PathVariable Long id, Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return commentRepository.findByAuthor(user, pageable).map(CommentDTO::fromComment);
    }

}
