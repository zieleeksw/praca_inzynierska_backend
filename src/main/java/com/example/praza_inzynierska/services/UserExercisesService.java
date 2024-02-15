package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.BaseAppExercises;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.models.UserExercise;
import com.example.praza_inzynierska.repositories.BaseAppExercisesRepository;
import com.example.praza_inzynierska.repositories.UserExerciseRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.UserExerciseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserExercisesService {

    private final UserExerciseRepository userExerciseRepository;
    private final UserRepository userRepository;
    private final BaseAppExercisesRepository baseAppExercisesRepository;

    public ResponseEntity<List<UserExercise>> fetchUserExercises(Long userId) {
        try {
            List<UserExercise> userExercises = userExerciseRepository.findByUserId(userId);
            return new ResponseEntity<>(userExercises, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> addUserExercise(UserExerciseRequest model) {
        try {
            Optional<User> user = userRepository.findById(model.getUserId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<UserExercise> userExerciseOptional = Optional.ofNullable(userExerciseRepository.findByName(model.getName()));
            if (userExerciseOptional.isPresent()) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            Optional<BaseAppExercises> baseAppExercisesOptional = Optional.ofNullable(baseAppExercisesRepository.findByName(model.getName()));
            if (baseAppExercisesOptional.isPresent()) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            UserExercise userExercise = getUserExercise(user.get(), model);
            userExerciseRepository.save(userExercise);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserExercise getUserExercise(User user, UserExerciseRequest model) {
        return UserExercise.builder()
                .user(user)
                .name(model.getName())
                .build();
    }

    public ResponseEntity<Void> deleteUserExercise(Long userId) {
        try {
            Optional<UserExercise> optional = userExerciseRepository.findById(userId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            userExerciseRepository.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
