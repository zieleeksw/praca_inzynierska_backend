package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.Training;
import com.example.praza_inzynierska.request.models.TrainingRequestModel;
import com.example.praza_inzynierska.services.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping("/{userId}/trainings")
    public ResponseEntity<List<Training>> getTrainingsByUserId(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId);
    }

    @PostMapping("/{userId}/trainings")
    public ResponseEntity<Void> addTrainingToUser(@PathVariable Long userId, @RequestBody TrainingRequestModel model) {
        return trainingService.addTrainingToUser(userId, model);
    }

    @DeleteMapping("/{userId}/trainings")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long trainingId) {
        return trainingService.deleteTraining(trainingId);
    }
}
