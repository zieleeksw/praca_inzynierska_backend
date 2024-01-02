package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/usernames")
    public ResponseEntity<List<String>> getAllUsers() {
        return userService.getAllLogins();
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        return userService.getAllEmails();
    }
}
