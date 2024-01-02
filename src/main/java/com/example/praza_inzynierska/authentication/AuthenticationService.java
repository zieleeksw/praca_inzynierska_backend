package com.example.praza_inzynierska.authentication;

import com.example.praza_inzynierska.models.AuthenticationResponseModel;
import com.example.praza_inzynierska.models.Role;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        return "Registered successfully";
    }

    public AuthenticationResponseModel authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Optional<User> user = repository.findByEmail(request.getEmail());
        Optional<UserAuth> userAuth = Optional.of(new UserAuth(user.get()));
        String jwtToken = jwtService.generateToken(userAuth.get());
        return AuthenticationResponseModel.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .email(user.get().getEmail())
                .token(jwtToken)
                .build();
    }
}
