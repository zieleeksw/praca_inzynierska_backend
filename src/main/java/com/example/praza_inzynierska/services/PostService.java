package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.Utils;
import com.example.praza_inzynierska.models.Post;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.PostRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.FollowPostRequestModel;
import com.example.praza_inzynierska.request.models.PostRequestModel;
import com.example.praza_inzynierska.response_models.PostResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Void> addPost(PostRequestModel model) {
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
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Post.builder()
                .author(user)
                .content(content)
                .timestamp(Utils.getTimeStamp())
                .build();
    }

    public ResponseEntity<List<PostResponseModel>> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            List<PostResponseModel> postResponseModels = posts.stream()
                    .map(post -> new PostResponseModel(
                            post.getId(),
                            post.getAuthor().getId(),
                            post.getAuthor().getUsername(),
                            post.getContent(),
                            post.getTimestamp(),
                            post.getFollowers()))
                    .toList();
            return new ResponseEntity<>(postResponseModels, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> follow(FollowPostRequestModel model) {
        try {
            Optional<Post> optional = postRepository.findById(model.getPostId());
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<User> user = userRepository.findById(model.getUserId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Post post = optional.get();
            if (post.getFollowers().contains(model.getUserId())) {
                post.getFollowers().remove(model.getUserId());
            } else {
                post.getFollowers().add(model.getUserId());
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
