package com.example.praza_inzynierska.training.controllers;


import com.example.praza_inzynierska.training.dto.UserExerciseRequest;
import com.example.praza_inzynierska.training.models.UserExercise;
import com.example.praza_inzynierska.training.services.UserExercisesService;
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

    @PostMapping("/exercises")
    public ResponseEntity<Boolean> addUserExercise(@RequestBody UserExerciseRequest userExerciseRequest) {
        return userExercisesService.addUserExercise(userExerciseRequest);
    }

    @DeleteMapping("/{exerciseId}/exercises")
    public ResponseEntity<Void> deleteUserExercise(@PathVariable Long exerciseId) {
        return userExercisesService.deleteUserExercise(exerciseId);
    }
}
