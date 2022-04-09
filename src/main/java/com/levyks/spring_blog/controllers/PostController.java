package com.levyks.spring_blog.controllers;

import com.levyks.spring_blog.dtos.BasicMessageDTO;
import com.levyks.spring_blog.dtos.comments.CommentDTO;
import com.levyks.spring_blog.dtos.comments.CreateCommentRequestDTO;
import com.levyks.spring_blog.dtos.posts.CreatePostRequestDTO;
import com.levyks.spring_blog.dtos.posts.PostDTO;
import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.models.Post;
import com.levyks.spring_blog.repositories.CommentRepository;
import com.levyks.spring_blog.repositories.PostRepository;
import com.levyks.spring_blog.security.details.UserDetailsImpl;
import com.levyks.spring_blog.security.details.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public PostController(PostRepository postRepository, CommentRepository commentRepository, UserDetailsServiceImpl userDetailsService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("")
    public Page<PostDTO> getAllPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findAll(pageable).map(PostDTO::fromPost);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public PostDTO createPost(
            @RequestBody @Valid CreatePostRequestDTO createPostRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Post post = new Post();
        post.setTitle(createPostRequestDTO.getTitle());
        post.setContent(createPostRequestDTO.getContent());
        post.setAuthor(userDetailsService.getUserByDetails(userDetails));
        postRepository.save(post);

        return PostDTO.fromPost(post);
    }

    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable Long id) {
        return postRepository.findById(id).map(PostDTO::fromPost)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public BasicMessageDTO deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pOST not found"));

        if(!userDetails.getEmail().equals(post.getAuthor().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
        }

        postRepository.delete(post);

        return new BasicMessageDTO("Post deleted");

    }

    @GetMapping("/{id}/comments")
    public Page<CommentDTO> getCommentsByPostId(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return commentRepository.findByPostId(id, pageable).map(CommentDTO::fromComment);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public CommentDTO createComment(
            @PathVariable Long id,
            @RequestBody @Valid CreateCommentRequestDTO createCommentRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment comment = new Comment();
        comment.setContent(createCommentRequestDTO.getContent());
        comment.setAuthor(userDetailsService.getUserByDetails(userDetails));
        comment.setPost(post);

        commentRepository.save(comment);

        return CommentDTO.fromComment(comment);
    }




}
