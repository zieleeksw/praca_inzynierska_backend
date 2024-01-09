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
}