package com.example.praza_inzynierska.training.services;

import com.example.praza_inzynierska.training.dto.AddTrainingBlockRequest;
import com.example.praza_inzynierska.training.dto.ExerciseToTrainingRequest;
import com.example.praza_inzynierska.training.dto.TrainingRequest;
import com.example.praza_inzynierska.training.models.Exercise;
import com.example.praza_inzynierska.training.models.Training;
import com.example.praza_inzynierska.training.models.TrainingExercise;
import com.example.praza_inzynierska.training.repositories.ExerciseRepository;
import com.example.praza_inzynierska.training.repositories.TrainingExerciseRepository;
import com.example.praza_inzynierska.training.repositories.TrainingRepository;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;
    private final ExerciseRepository exerciseRepository;

    public ResponseEntity<List<Training>> getTrainingsByUserId(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Training> trainings = trainingRepository.findByUserId(id);
            return new ResponseEntity<>(trainings, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> addTrainingToUser(TrainingRequest model) {
        try {
            Optional<User> user = userRepository.findById(model.getUserId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<Training> trainingOptional = trainingRepository.findByName(model.getName());
            if (trainingOptional.isPresent()) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            Training training = getTraining(user.get(), model);
            trainingRepository.save(training);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Training getTraining(User user, TrainingRequest model) {
        return Training.builder()
                .user(user)
                .name(model.getName())
                .build();
    }

    public ResponseEntity<Void> deleteTraining(Long trainingId) {
        try {
            Optional<Training> optional = trainingRepository.findById(trainingId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            trainingRepository.deleteById(trainingId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Training> fetchTrainingById(Long trainingId) {
        try {
            Optional<Training> optional = trainingRepository.findById(trainingId);
            return optional.map(training -> new ResponseEntity<>(training, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteExerciseFromTraining(Long exerciseId) {
        try {
            Optional<TrainingExercise> optional = trainingExerciseRepository.findById(exerciseId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            trainingRepository.deleteById(exerciseId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteExerciseFromTrainingByName(Long trainingId, String exerciseName) {
        try {
            Optional<Training> trainingOptional = trainingRepository.findById(trainingId);
            if (trainingOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<TrainingExercise> exercisesToRemove = getTrainingExercisesToRemove(trainingOptional.get().getExercises(), exerciseName);
            trainingExerciseRepository.deleteAll(exercisesToRemove);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<TrainingExercise> getTrainingExercisesToRemove(List<TrainingExercise> trainingExercises, String exerciseName) {
        return trainingExercises.stream()
                .filter(exercise -> exercise.getName().equals(exerciseName))
                .toList();
    }

    public ResponseEntity<List<TrainingExercise>> fetchExercisesByTrainingIdAndName(Long trainingId, String exerciseName) {
        try {
            return new ResponseEntity<>(trainingExerciseRepository.findByTrainingIdAndName(trainingId, exerciseName), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> addExerciseToTraining(ExerciseToTrainingRequest request) {
        try {
            Optional<Training> trainingOptional = trainingRepository.findById(request.getTrainingId());
            if (trainingOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            TrainingExercise exercise = getTrainingExercise(request, trainingOptional.get());
            trainingExerciseRepository.save(exercise);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private TrainingExercise getTrainingExercise(ExerciseToTrainingRequest exerciseToTrainingRequest, Training training) {
        return TrainingExercise.builder()
                .name(exerciseToTrainingRequest.getName())
                .repetition(exerciseToTrainingRequest.getRepetition())
                .weight(exerciseToTrainingRequest.getWeight())
                .training(training)
                .build();
    }

    public ResponseEntity<Void> addTrainingBlockToDay(AddTrainingBlockRequest addTrainingBlockRequest) {
        try {
            Optional<Training> trainingOptional = trainingRepository.findById(addTrainingBlockRequest.getTrainingId());
            if (trainingOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<User> userOptional = userRepository.findById(addTrainingBlockRequest.getUserId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<TrainingExercise> trainingExercises = trainingOptional.get().getExercises();
            trainingExercises.forEach(trainingExercise -> {
                Exercise exercise = mapTrainingExerciseToExercise(trainingExercise, addTrainingBlockRequest.getDate(), userOptional.get());
                exerciseRepository.save(exercise);
            });
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Exercise mapTrainingExerciseToExercise(TrainingExercise trainingExercise, String date, User user) {
        return Exercise.builder()
                .name(trainingExercise.getName())
                .user(user)
                .date(date)
                .repetition(trainingExercise.getRepetition())
                .weight(trainingExercise.getWeight())
                .build();
    }
}
