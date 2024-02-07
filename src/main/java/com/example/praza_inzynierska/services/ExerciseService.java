package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.Exercise;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.ExerciseRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.ExerciseRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Void> addExercise(ExerciseRequestModel model) {
        try {
            Optional<User> user = userRepository.findById(model.getUserId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Exercise exercise = getExercise(user.get(), model);
            exerciseRepository.save(exercise);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Exercise getExercise(User user, ExerciseRequestModel model) {
        return Exercise.builder()
                .user(user)
                .name(model.getName())
                .date(model.getDate())
                .repetition(model.getRepetition())
                .weight(model.getWeight())
                .build();
    }

    public ResponseEntity<List<Exercise>> fetchExercisesByDateAndName(Long id, String date, String name) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Exercise> exercises = exerciseRepository.findByDateAndUserIdAndName(date, id, name);
            return new ResponseEntity<>(exercises, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Exercise>> fetchExercisesByDate(Long id, String date) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Exercise> exercises = exerciseRepository.findByDateAndUserId(date, id);
            return new ResponseEntity<>(exercises, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteExerciseById(Long id) {
        try {
            Optional<Exercise> optional = exerciseRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            exerciseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteExerciseByDateAndName(String date, String name) {
        try {

            exerciseRepository.deleteByDateAndName(date, name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
