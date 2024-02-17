package com.example.praza_inzynierska.controllers;


import com.example.praza_inzynierska.models.UserExercise;
import com.example.praza_inzynierska.request.models.UserExerciseRequest;
import com.example.praza_inzynierska.services.UserExercisesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserExercisesController {

    private final UserExercisesService userExercisesService;

    @GetMapping("/{userId}/exercises")
    public ResponseEntity<List<UserExercise>> fetchUserExercises(@PathVariable Long userId) {
        return userExercisesService.fetchUserExercises(userId);
    }

    @PostMapping("/{userId}/exercises")
    public ResponseEntity<Boolean> addUserExercise(@RequestBody UserExerciseRequest userExerciseRequest) {
        return userExercisesService.addUserExercise(userExerciseRequest);
    }

    @DeleteMapping("/{userId}/exercises")
    public ResponseEntity<Void> deleteUserExercise(@PathVariable Long userId) {
        return userExercisesService.deleteUserExercise(userId);
    }
}
