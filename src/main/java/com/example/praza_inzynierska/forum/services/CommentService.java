package com.example.praza_inzynierska.forum.services;

import com.example.praza_inzynierska.forum.dto.CommentRequest;
import com.example.praza_inzynierska.forum.dto.CommentResponse;
import com.example.praza_inzynierska.forum.models.Comment;
import com.example.praza_inzynierska.forum.models.Post;
import com.example.praza_inzynierska.forum.repositories.CommentRepository;
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
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<List<CommentResponse>> fetchAllComments(Long postId) {
        try {
            Optional<Post> optional = postRepository.findById(postId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Post post = optional.get();
            List<CommentResponse> listOfCommentsResponseModel = getListOfComments(post);
            return new ResponseEntity<>(listOfCommentsResponseModel, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<CommentResponse> getListOfComments(Post post) {
        return post.getComments().stream()
                .map(this::mapCommentToResponseModel)
                .toList();
    }

    private CommentResponse mapCommentToResponseModel(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .username(comment.getAuthor().getUsername())
                .content(comment.getContent())
                .timestamp(comment.getTimestamp())
                .build();
    }

    public ResponseEntity<Void> addComment(CommentRequest model) {
        try {
            Optional<Post> postOptional = postRepository.findById(model.getPostId());
            if (postOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<User> userOptional = userRepository.findById(model.getAuthorId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            User user = userOptional.get();
            Post post = postOptional.get();
            Comment comment = getComment(user, post, model.getContent());
            commentRepository.save(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteCommentById(Long commentId) {
        try {
            Optional<Comment> optional = commentRepository.findById(commentId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            commentRepository.deleteById(commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Comment getComment(User user, Post post, String content) {
        return Comment.builder()
                .author(user)
                .post(post)
                .content(content)
                .timestamp(Utils.getTimeStamp())
                .build();
    }
}
