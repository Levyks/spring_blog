package com.levyks.spring_blog.controllers;

import com.levyks.spring_blog.dtos.misc.BasicMessageDTO;
import com.levyks.spring_blog.dtos.comments.CommentDTO;
import com.levyks.spring_blog.dtos.comments.CreateOrUpdateCommentRequestDTO;
import com.levyks.spring_blog.dtos.posts.BasicPostDTO;
import com.levyks.spring_blog.dtos.posts.CreateOrUpdatePostRequestDTO;
import com.levyks.spring_blog.dtos.posts.PostDTO;
import com.levyks.spring_blog.models.Category;
import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.models.Post;
import com.levyks.spring_blog.repositories.CategoryRepository;
import com.levyks.spring_blog.repositories.CommentRepository;
import com.levyks.spring_blog.repositories.PostRepository;
import com.levyks.spring_blog.security.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PostController(PostRepository postRepository, CommentRepository commentRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("")
    public Page<BasicPostDTO> getPosts(
            @RequestParam(value="q", required = false) String query,
            Pageable pageable
    ) {
        return query == null ?
                postRepository.findAll(pageable).map(BasicPostDTO::fromPost) :
                postRepository.findByTitleContainingIgnoreCase(query, pageable).map(BasicPostDTO::fromPost);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public PostDTO createPost(
            @RequestBody @Valid CreateOrUpdatePostRequestDTO createPostRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Category category = categoryRepository.findById(createPostRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Post post = new Post();
        post.setTitle(createPostRequestDTO.getTitle());
        post.setContent(createPostRequestDTO.getContent());
        post.setAuthor(userDetails.getUser());
        post.setCategory(category);

        return PostDTO.fromPost(postRepository.save(post));
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable Long id) {
        return postRepository.findById(id).map(PostDTO::fromPost)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public PostDTO updatePost(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdatePostRequestDTO updatePostRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Category category = categoryRepository.findById(updatePostRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if(!post.isAuthor(userDetails.getUser()) && !userDetails.hasRole("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this post");
        }

        post.setTitle(updatePostRequestDTO.getTitle());
        post.setContent(updatePostRequestDTO.getContent());
        post.setCategory(category);
        post.setEdited(true);

        return PostDTO.fromPost(postRepository.save(post));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public BasicMessageDTO deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if(!post.isAuthor(userDetails.getUser()) && !userDetails.hasRole("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
        }

        postRepository.delete(post);

        return new BasicMessageDTO("Post deleted");

    }

    @GetMapping("/{id}/comments")
    public Page<CommentDTO> getCommentsByPost(
        @PathVariable Long id,
        Pageable pageable
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return commentRepository.findByPost(post, pageable).map(CommentDTO::fromComment);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public CommentDTO createComment(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateCommentRequestDTO createCommentRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment comment = new Comment();
        comment.setContent(createCommentRequestDTO.getContent());
        comment.setAuthor(userDetails.getUser());
        comment.setPost(post);

        return CommentDTO.fromComment(commentRepository.save(comment));
    }




}
