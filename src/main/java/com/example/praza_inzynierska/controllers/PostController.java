package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.request.models.PostRequestModel;
import com.example.praza_inzynierska.response_models.PostResponseModel;
import com.example.praza_inzynierska.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/add")
    public ResponseEntity<Void> addPost(@RequestBody PostRequestModel model) {
        return postService.addPost(model);
    }

    @PostMapping("/follow/userId/{userId}/postId/{postId}")
    public ResponseEntity<Void> followPost(@PathVariable Long userId,
                                           @PathVariable Long postId) {
        return postService.follow(userId, postId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return postService.deletePostById(id);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseModel>> fetchAllPosts() {
        return postService.getAllPosts();
    }
}