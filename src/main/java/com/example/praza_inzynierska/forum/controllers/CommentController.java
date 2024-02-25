package com.example.praza_inzynierska.forum.controllers;

import com.example.praza_inzynierska.forum.dto.CommentRequest;
import com.example.praza_inzynierska.forum.dto.CommentResponse;
import com.example.praza_inzynierska.forum.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentResponse>> fetchCommentsFromPost(@PathVariable Long id) {
        return commentService.fetchAllComments(id);
    }

    @PostMapping()
    public ResponseEntity<Void> addComment(@RequestBody CommentRequest model) {
        return commentService.addComment(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return commentService.deleteCommentById(id);
    }
}
