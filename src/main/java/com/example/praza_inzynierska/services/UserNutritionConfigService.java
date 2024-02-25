package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.NutritionConfig;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.NutritionConfigRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserNutritionConfigService {

    private final UserRepository userRepository;
    private final NutritionConfigRepository userNutritionConfigRepository;

    public ResponseEntity<String> addUserNutritionConfig(long userId, NutritionConfig nutritionConfig) {
        try {
            Optional<User> user = userRepository.findById(userId);
            nutritionConfig.setUser(user.get());
            nutritionConfig.setId(userId);
            userNutritionConfigRepository.save(nutritionConfig);
            return ResponseEntity.ok("User nutrition config added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user nutrition config.");
        }
    }

    public ResponseEntity<NutritionConfig> fetchCaloriesNeeded(long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            NutritionConfig config = userNutritionConfigRepository.findByUserId(userId);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Void> changeActivityLevel(long userId, String activityLevel) {
        return changeUserNutritionConfig(userId, config -> config.setActivityLevel(activityLevel));
    }

    public ResponseEntity<Void> changeCurrentWeight(long userId, Double currentWeight) {
        return changeUserNutritionConfig(userId, config -> config.setCurrentWeight(currentWeight));
    }

    public ResponseEntity<Void> changeTargetWeight(long userId, Double targetWeight) {
        return changeUserNutritionConfig(userId, config -> config.setTargetWeight(targetWeight));
    }

    private ResponseEntity<Void> changeUserNutritionConfig(long userId, Consumer<NutritionConfig> configUpdater) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            NutritionConfig config = userNutritionConfigRepository.findByUserId(userId);
            if (config == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            configUpdater.accept(config);
            userNutritionConfigRepository.save(config);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}