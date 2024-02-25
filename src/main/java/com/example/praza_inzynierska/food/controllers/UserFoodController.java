package com.example.praza_inzynierska.food.controllers;

import com.example.praza_inzynierska.food.dto.UserFoodRequest;
import com.example.praza_inzynierska.food.models.UserFood;
import com.example.praza_inzynierska.food.services.UserFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserFoodController {

    private final UserFoodService userFoodService;

    @GetMapping("/{userId}/food")
    public ResponseEntity<List<UserFood>> fetchUserFood(@PathVariable Long userId) {
        return userFoodService.fetchUserFood(userId);
    }

    @PostMapping("/food")
    public ResponseEntity<Boolean> addUserFood(@RequestBody UserFoodRequest userFoodRequest) {
        return userFoodService.addUserFood(userFoodRequest);
    }

    @DeleteMapping("/{userId}/food/{foodName}")
    public ResponseEntity<Void> deleteUserFoodByName(@PathVariable Long userId,
                                                     @PathVariable String foodName) {
        return userFoodService.deleteUserFoodByName(userId, foodName);
    }
}
