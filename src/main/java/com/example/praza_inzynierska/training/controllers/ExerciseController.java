package com.example.praza_inzynierska.training.controllers;

import com.example.praza_inzynierska.training.dto.ExerciseRequest;
import com.example.praza_inzynierska.training.models.Exercise;
import com.example.praza_inzynierska.training.services.ExerciseService;
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
    public ResponseEntity<Void> addExercise(@RequestBody ExerciseRequest model) {
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

    @GetMapping("/user/{userId}/chart/{date}/{name}")
    public ResponseEntity<List<Exercise>> fetchChartExercises(@PathVariable Long userId,
                                                              @PathVariable String date,
                                                              @PathVariable String name) {
        return exerciseService.fetchChartExercises(userId, date, name);
    }
}
