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

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> isEmailAvailable(@PathVariable String email) {
        return userService.isEmailAvailable(email);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable String username) {
        return userService.isUsernameAvailable(username);
    }
}
