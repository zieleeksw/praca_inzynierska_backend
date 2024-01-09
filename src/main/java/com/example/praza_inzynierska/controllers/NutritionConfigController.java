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
                                                         @RequestBody
                                                         NutritionConfig nutritionConfig) {
        return userNutritionConfigService.addUserNutritionConfig(userId, nutritionConfig);

    }
}