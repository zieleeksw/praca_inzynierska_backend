package com.example.praza_inzynierska.forum;

import com.example.praza_inzynierska.forum.dto.PostRequest;
import com.example.praza_inzynierska.forum.dto.PostResponse;
import com.example.praza_inzynierska.forum.models.Post;
import com.example.praza_inzynierska.forum.repositories.PostRepository;
import com.example.praza_inzynierska.forum.services.PostService;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void addPost_UserNotFound_ShouldReturnInternalServerError() {
        PostRequest model = new PostRequest();
        when(userRepository.findById(model.getAuthorId())).thenReturn(Optional.empty());

        ResponseEntity<Void> response = postService.addPost(model);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void addPost_Success_ShouldReturnOk() {
        PostRequest model = new PostRequest();
        User user = new User();
        Post post = new Post();
        when(userRepository.findById(model.getAuthorId())).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        ResponseEntity<Void> response = postService.addPost(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addPost_ExceptionThrown_ShouldReturnInternalServerError() {
        PostRequest model = new PostRequest();
        when(userRepository.findById(model.getAuthorId())).thenThrow(RuntimeException.class);

        ResponseEntity<Void> response = postService.addPost(model);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void follow_UserNotFollowing_ShouldFollowPost() {
        Long userId = 1L;
        Long postId = 1L;
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setFollowers(new ArrayList<>());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = postService.follow(userId, postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(post.getFollowers().contains(userId));
    }

    @Test
    void follow_UserFollowing_ShouldUnfollow() {
        Long userId = 1L;
        Long postId = 1L;
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setFollowers(new ArrayList<>());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = postService.follow(userId, postId);
        ResponseEntity<Void> response1 = postService.follow(userId, postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertTrue(post.getFollowers().isEmpty());
    }

    @Test
    public void getAllPosts_Success_ShouldReturnPosts() {
        User author = new User();
        List<Post> posts = Arrays.asList(
                Post.builder().author(author).build(),
                Post.builder().author(author).build()
        );
        when(postRepository.findAll()).thenReturn(posts);
        ResponseEntity<List<PostResponse>> response = postService.getAllPosts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void getAllPosts_ExceptionThrown_ShouldReturnInternalServerError() {
        when(postRepository.findAll()).thenThrow(RuntimeException.class);
        ResponseEntity<List<PostResponse>> response = postService.getAllPosts();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void follow_PostOrUserNotFound_ShouldReturnInternalServerError() {
        Long userId = 1L, postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = postService.follow(userId, postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void follow_Success_ShouldToggleFollowAndReturnOk() {
        Long userId = 1L, postId = 1L;
        User user = new User();
        Post post = Post.builder().author(user).followers(new ArrayList<>()).build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = postService.follow(userId, postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void follow_ExceptionThrown_ShouldReturnInternalServerError() {
        Long userId = 1L, postId = 1L;
        when(postRepository.findById(postId)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = postService.follow(userId, postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deletePostById_PostNotFound_ShouldReturnInternalServerError() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = postService.deletePostById(postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deletePostById_Success_ShouldReturnOk() {
        Long postId = 1L;
        Post post = new Post(1L, new User(), "content", "timestamp", new ArrayList<>(), new ArrayList<>());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(postId);
        ResponseEntity<Void> response = postService.deletePostById(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deletePostById_ExceptionThrown_ShouldReturnInternalServerError() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = postService.deletePostById(postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void follow_UserNotFound_ShouldReturnInternalServerError() {
        Long userId = 1L;
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = postService.follow(userId, postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
