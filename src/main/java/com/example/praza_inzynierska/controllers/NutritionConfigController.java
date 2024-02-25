package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.NutritionConfig;
import com.example.praza_inzynierska.services.UserNutritionConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-nutrition-config")
@RequiredArgsConstructor
public class NutritionConfigController {

    private final UserNutritionConfigService userNutritionConfigService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<String> addUserNutritionConfig(@PathVariable long userId,
                                                         @RequestBody NutritionConfig nutritionConfig) {
        return userNutritionConfigService.addUserNutritionConfig(userId, nutritionConfig);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<NutritionConfig> fetchCaloriesNeeded(@PathVariable long userId) {
        return userNutritionConfigService.fetchCaloriesNeeded(userId);
    }

    @PostMapping("/{userId}/activity_level/{activity_level}")
    public ResponseEntity<Void> changeActivityLevel(@PathVariable("userId") long userId,
                                                    @PathVariable("activity_level") String activityLevel) {
        return userNutritionConfigService.changeActivityLevel(userId, activityLevel);
    }

    @PostMapping("/{userId}/current_weight/{current_weight}")
    public ResponseEntity<Void> changeCurrentWeight(@PathVariable("userId") long userId,
                                                    @PathVariable("current_weight") Double currentWeight) {
        return userNutritionConfigService.changeCurrentWeight(userId, currentWeight);
    }

    @PostMapping("/{userId}/target_weight/{target_weight}")
    public ResponseEntity<Void> changeTargetWeight(@PathVariable("userId") long userId,
                                                   @PathVariable("target_weight") Double targetWeight) {
        return userNutritionConfigService.changeTargetWeight(userId, targetWeight);
    }
}