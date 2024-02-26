package com.example.praza_inzynierska.authentication;

import com.example.praza_inzynierska.authentication.dto.AuthenticationRequest;
import com.example.praza_inzynierska.authentication.dto.AuthenticationResponse;
import com.example.praza_inzynierska.authentication.dto.RegisterRequest;
import com.example.praza_inzynierska.authentication.models.UserAuth;
import com.example.praza_inzynierska.authentication.services.AuthenticationService;
import com.example.praza_inzynierska.authentication.services.JwtService;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authService;

    @Test
    void register_CreatesUser_ReturnsSuccessMessage() {
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "password");
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        String result = authService.register(request);
        assertEquals("Registered successfully", result);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void authenticate_AuthenticatesUser_ReturnsAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest("email@example.com", "password");
        User user = User.builder()
                .id(1L)
                .username("username")
                .email("email@example.com")
                .password("password")
                .userNutritionConfig(null)
                .build();
        String jwtToken = "jwtToken";
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserAuth.class))).thenReturn(jwtToken);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        AuthenticationResponse response = authService.authenticate(request);
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(jwtToken, response.getToken());
    }
}
