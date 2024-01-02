package com.example.praza_inzynierska.services;


import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<List<String>> getAllLogins() {
        try {
            List<String> logins = getLogins();
            return new ResponseEntity<>(logins, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<String>> getAllEmails() {
        try {
            List<String> emails = getEmails();
            return new ResponseEntity<>(emails, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> getLogins() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    private List<String> getEmails() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }
}
