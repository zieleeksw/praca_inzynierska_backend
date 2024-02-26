package com.example.praza_inzynierska.user;

import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import com.example.praza_inzynierska.user.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void isEmailAvailable_WhenEmailIsAvailable_ReturnsTrue() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ResponseEntity<Boolean> response = userService.isEmailAvailable(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void isEmailAvailable_WhenEmailIsNotAvailable_ReturnsFalse() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        ResponseEntity<Boolean> response = userService.isEmailAvailable(email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.FALSE, response.getBody());
    }

    @Test
    void isEmailAvailable_WhenExceptionOccurs_ReturnsInternalServerError() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenThrow(RuntimeException.class);
        ResponseEntity<Boolean> response = userService.isEmailAvailable(email);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void isUsernameAvailable_WhenUsernameIsAvailable_ReturnsTrue() {
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = userService.isUsernameAvailable(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void isUsernameAvailable_WhenUsernameIsNotAvailable_ReturnsFalse() {
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        ResponseEntity<Boolean> response = userService.isUsernameAvailable(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(Boolean.TRUE.equals(response.getBody()));
    }

    @Test
    void isUsernameAvailable_WhenExceptionOccurs_ReturnsInternalServerError() {
        String username = "username";
        when(userRepository.findByUsername(username)).thenThrow(RuntimeException.class);
        ResponseEntity<Boolean> response = userService.isUsernameAvailable(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}

