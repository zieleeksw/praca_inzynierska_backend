package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.Exercise;
import com.example.praza_inzynierska.request.models.ExerciseRequestModel;
import com.example.praza_inzynierska.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping()
    public ResponseEntity<Void> addExercise(@RequestBody ExerciseRequestModel model) {
        return exerciseService.addExercise(model);
    }

    @GetMapping("/user/{id}/date/{date}/name/{name}")
    public ResponseEntity<List<Exercise>> fetchExercisesByName(@PathVariable Long id,
                                                               @PathVariable String date,
                                                               @PathVariable String name) {
        return exerciseService.fetchExercisesByDateAndName(id, date, name);
    }

    @GetMapping("/user/{id}/date/{date}")
    public ResponseEntity<List<Exercise>> fetchExercises(@PathVariable Long id,
                                                         @PathVariable String date) {
        return exerciseService.fetchExercisesByDate(id, date);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        return exerciseService.deleteExerciseById(id);
    }

    @DeleteMapping("/date/{date}/name/{name}")
    public ResponseEntity<Void> deleteExerciseByDateAndName(@PathVariable String date, @PathVariable String name) {
        return exerciseService.deleteExerciseByDateAndName(date, name);
    }

    @GetMapping("/available/{userId}")
    public ResponseEntity<List<String>> fetchAvailableExercises(@PathVariable long userId) {
        return exerciseService.fetchAvailableExercises(userId);
    }
}
