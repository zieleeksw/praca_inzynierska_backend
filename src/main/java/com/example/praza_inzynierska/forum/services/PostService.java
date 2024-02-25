package com.example.praza_inzynierska.forum.services;

import com.example.praza_inzynierska.forum.dto.PostRequest;
import com.example.praza_inzynierska.forum.dto.PostResponse;
import com.example.praza_inzynierska.forum.models.Post;
import com.example.praza_inzynierska.forum.repositories.PostRepository;
import com.example.praza_inzynierska.others.Utils;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Void> addPost(PostRequest model) {
        try {
            Optional<User> user = userRepository.findById(model.getAuthorId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Post post = getPost(user.get(), model.getContent());
            post.setAuthor(user.get());
            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Post getPost(User user, String content) {
        return Post.builder()
                .author(user)
                .content(content)
                .timestamp(Utils.getTimeStamp())
                .build();
    }

    public ResponseEntity<List<PostResponse>> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            List<PostResponse> postResponses = posts.stream()
                    .map(post -> new PostResponse(
                            post.getId(),
                            post.getAuthor().getId(),
                            post.getAuthor().getUsername(),
                            post.getContent(),
                            post.getTimestamp(),
                            post.getFollowers()))
                    .toList();
            return new ResponseEntity<>(postResponses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> follow(Long userId, Long postId) {
        try {
            Optional<Post> optional = postRepository.findById(postId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Post post = optional.get();
            if (post.getFollowers().contains(userId)) {
                post.getFollowers().remove(userId);
            } else {
                post.getFollowers().add(userId);
            }
            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deletePostById(Long id) {
        try {
            Optional<Post> optional = postRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            postRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
