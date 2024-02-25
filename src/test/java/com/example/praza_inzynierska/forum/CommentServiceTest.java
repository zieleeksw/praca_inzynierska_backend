package com.example.praza_inzynierska.forum;

import com.example.praza_inzynierska.forum.dto.CommentRequest;
import com.example.praza_inzynierska.forum.dto.CommentResponse;
import com.example.praza_inzynierska.forum.models.Comment;
import com.example.praza_inzynierska.forum.models.Post;
import com.example.praza_inzynierska.forum.repositories.CommentRepository;
import com.example.praza_inzynierska.forum.repositories.PostRepository;
import com.example.praza_inzynierska.forum.services.CommentService;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void fetchAllComments_PostNotFound_ShouldReturnInternalServerError() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        ResponseEntity<List<CommentResponse>> response = commentService.fetchAllComments(postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void fetchAllComments_Success_ShouldReturnListOfComments() {
        Long postId = 1L;
        Post post = mock(Post.class);
        User author = new User();
        author.setId(1L);
        author.setUsername("testUser");
        Comment comment1 = new Comment();
        comment1.setAuthor(author);
        comment1.setId(1L);
        comment1.setContent("CONTENT");
        comment1.setTimestamp("TIMESTAMP");
        comment1.setPost(new Post());
        Comment comment2 = new Comment();
        comment2.setAuthor(author);
        when(post.getComments()).thenReturn(Arrays.asList(comment1, comment2));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        ResponseEntity<List<CommentResponse>> response = commentService.fetchAllComments(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void addComment_PostOrUserNotFound_ShouldReturnInternalServerError() {
        CommentRequest model = new CommentRequest();
        when(postRepository.findById(model.getPostId())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = commentService.addComment(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addComment_Success_ShouldReturnOk() {
        CommentRequest model = new CommentRequest();
        when(postRepository.findById(model.getPostId())).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(model.getAuthorId())).thenReturn(Optional.of(new User()));
        ResponseEntity<Void> response = commentService.addComment(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void deleteCommentById_CommentNotFound_ShouldReturnInternalServerError() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = commentService.deleteCommentById(commentId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteCommentById_Success_ShouldReturnOk() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));
        ResponseEntity<Void> response = commentService.deleteCommentById(commentId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void fetchAllComments_ThrowsException_ShouldReturnInternalServerError() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenThrow(RuntimeException.class);
        ResponseEntity<List<CommentResponse>> response = commentService.fetchAllComments(postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteCommentById_ThrowsException_ShouldReturnInternalServerError() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = commentService.deleteCommentById(commentId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addComment_UserNotFound_ShouldReturnInternalServerError() {
        CommentRequest model = new CommentRequest();
        model.setPostId(1L);
        model.setAuthorId(1L);
        when(postRepository.findById(model.getPostId())).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(model.getAuthorId())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = commentService.addComment(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addComment_ThrowsException_ShouldReturnInternalServerError() {
        CommentRequest model = new CommentRequest();
        model.setPostId(1L);
        model.setAuthorId(1L);
        when(postRepository.findById(model.getPostId())).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = commentService.addComment(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
