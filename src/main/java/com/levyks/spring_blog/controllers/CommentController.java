package com.levyks.spring_blog.controllers;

import com.levyks.spring_blog.dtos.BasicMessageDTO;
import com.levyks.spring_blog.dtos.comments.CommentDTO;
import com.levyks.spring_blog.dtos.comments.CreateCommentRequestDTO;
import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.repositories.CommentRepository;
import com.levyks.spring_blog.security.details.UserDetailsImpl;
import com.levyks.spring_blog.security.details.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CommentController(CommentRepository commentRepository, UserDetailsServiceImpl userDetailsService) {
        this.commentRepository = commentRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable Long id) {
        return commentRepository.findById(id).map(CommentDTO::fromComment)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public BasicMessageDTO deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if(!userDetails.getEmail().equals(comment.getAuthor().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);

        return new BasicMessageDTO("Comment deleted");

    }

}
