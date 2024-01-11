package com.example.praza_inzynierska.services;


import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<Boolean> isEmailAvailable(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            return new ResponseEntity<>(user.isEmpty(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> isUsernameAvailable(String email) {
        try {
            Optional<User> user = userRepository.findByUsername(email);
            return new ResponseEntity<>(user.isEmpty(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
