package com.levyks.spring_blog.controllers;

import com.levyks.spring_blog.dtos.misc.BasicMessageDTO;
import com.levyks.spring_blog.dtos.comments.CommentDTO;
import com.levyks.spring_blog.dtos.comments.CreateOrUpdateCommentRequestDTO;
import com.levyks.spring_blog.models.Comment;
import com.levyks.spring_blog.repositories.CommentRepository;
import com.levyks.spring_blog.security.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable Long id) {
        return commentRepository.findById(id).map(CommentDTO::fromComment)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CommentDTO updateComment(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateCommentRequestDTO updateCommentRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if(!comment.isAuthor(userDetails.getUser()) && !userDetails.hasRole("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this comment");
        }

        comment.setContent(updateCommentRequestDTO.getContent());
        comment.setEdited(true);

        return CommentDTO.fromComment(commentRepository.save(comment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public BasicMessageDTO deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if(!comment.isAuthor(userDetails.getUser()) && !userDetails.hasRole("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);

        return new BasicMessageDTO("Comment deleted");

    }

}
