package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.request.models.CommentRequestModel;
import com.example.praza_inzynierska.response_models.CommentResponseModel;
import com.example.praza_inzynierska.services.CommentService;
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
    public ResponseEntity<List<CommentResponseModel>> fetchCommentsFromPost(@PathVariable Long id) {
        return commentService.fetchAllComments(id);
    }

    @PostMapping()
    public ResponseEntity<Void> addComment(@RequestBody CommentRequestModel model) {
        return commentService.addComment(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return commentService.deleteCommentById(id);
    }
}
