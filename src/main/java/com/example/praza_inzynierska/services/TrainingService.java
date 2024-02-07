package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.Training;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.TrainingRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.TrainingRequestModel;
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

    public ResponseEntity<Void> addTrainingToUser(long userId, TrainingRequestModel model) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Training training = getTraining(user.get(), model);
            trainingRepository.save(training);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Training getTraining(User user, TrainingRequestModel model) {
        return Training.builder()
                .user(user)
                .name(model.getName())
                .date(model.getDate())
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
}
