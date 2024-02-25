package com.example.praza_inzynierska.training.services;

import com.example.praza_inzynierska.training.dto.ExerciseRequest;
import com.example.praza_inzynierska.training.models.BaseAppExercises;
import com.example.praza_inzynierska.training.models.Exercise;
import com.example.praza_inzynierska.training.models.UserExercise;
import com.example.praza_inzynierska.training.repositories.BaseAppExercisesRepository;
import com.example.praza_inzynierska.training.repositories.ExerciseRepository;
import com.example.praza_inzynierska.training.repositories.UserExerciseRepository;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final BaseAppExercisesRepository baseAppExercisesRepository;
    private final UserExerciseRepository userExerciseRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Void> addExercise(ExerciseRequest model) {
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

    private Exercise getExercise(User user, ExerciseRequest model) {
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

    public ResponseEntity<List<String>> fetchAvailableExercises(long userId) {
        try {
            List<String> baseAppExercises = baseAppExercisesRepository.findAll().stream().map(BaseAppExercises::getName).toList();
            List<String> result = new ArrayList<>(baseAppExercises);
            List<String> userExercises = userExerciseRepository.findByUserId(userId).stream().map(UserExercise::getName).toList();
            result.addAll(userExercises);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Exercise>> fetchChartExercises(Long userId, String date, String name) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<Exercise> targetMonthExercises = exerciseRepository.findByUserIdAndNameAndDateContaining(userId, name, date);


            List<Exercise> averages = targetMonthExercises.stream()
                    .collect(Collectors.groupingBy(Exercise::getDate))
                    .entrySet().stream()
                    .map(entry -> {
                        List<Exercise> exercises = entry.getValue();
                        double avgRepetition = exercises.stream().mapToInt(Exercise::getRepetition).average().orElse(0);
                        double avgWeight = exercises.stream().mapToDouble(Exercise::getWeight).average().orElse(0);
                        return Exercise.builder()
                                .date(entry.getKey())
                                .name(name)
                                .repetition((int) avgRepetition)
                                .weight(avgWeight)
                                .build();
                    }).collect(Collectors.toList());
            return new ResponseEntity<>(averages, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
