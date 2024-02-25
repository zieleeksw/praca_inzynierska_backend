package com.example.praza_inzynierska.training.controllers;

import com.example.praza_inzynierska.training.dto.AddTrainingBlockRequest;
import com.example.praza_inzynierska.training.dto.ExerciseToTrainingRequest;
import com.example.praza_inzynierska.training.dto.TrainingRequest;
import com.example.praza_inzynierska.training.models.Training;
import com.example.praza_inzynierska.training.models.TrainingExercise;
import com.example.praza_inzynierska.training.services.TrainingService;
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

    @PostMapping("/trainings")
    public ResponseEntity<Boolean> addTrainingToUser(@RequestBody TrainingRequest model) {
        return trainingService.addTrainingToUser(model);
    }

    @DeleteMapping("/trainings/{trainingId}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long trainingId) {
        return trainingService.deleteTraining(trainingId);
    }

    @GetMapping("/trainings/{trainingId}")
    public ResponseEntity<Training> fetchTrainingById(@PathVariable Long trainingId) {
        return trainingService.fetchTrainingById(trainingId);
    }

    @DeleteMapping("/trainings/exercise/{exerciseId}")
    public ResponseEntity<Void> deleteExerciseFromTraining(@PathVariable Long exerciseId) {
        return trainingService.deleteExerciseFromTraining(exerciseId);
    }

    @DeleteMapping("/trainings/{trainingId}/exercise/{exerciseName}")
    public ResponseEntity<Void> deleteExerciseFromTrainingByName(
            @PathVariable("trainingId") Long trainingId,
            @PathVariable("exerciseName") String exerciseName) {
        return trainingService.deleteExerciseFromTrainingByName(trainingId, exerciseName);
    }

    @GetMapping("/trainings/{trainingId}/{exerciseName}")
    public ResponseEntity<List<TrainingExercise>> fetchExercisesByTrainingIdAndName(
            @PathVariable("trainingId") Long trainingId,
            @PathVariable("exerciseName") String exerciseName) {
        return trainingService.fetchExercisesByTrainingIdAndName(trainingId, exerciseName);
    }

    @PostMapping("/trainings/exercise")
    public ResponseEntity<Void> addExerciseToTraining(@RequestBody ExerciseToTrainingRequest exerciseToTrainingRequest) {
        return trainingService.addExerciseToTraining(exerciseToTrainingRequest);
    }


    @PostMapping("/trainings/block")
    public ResponseEntity<Void> addTrainingBlockToDay(@RequestBody AddTrainingBlockRequest addTrainingBlockRequest) {
        return trainingService.addTrainingBlockToDay(addTrainingBlockRequest);

    }
}
