package com.example.praza_inzynierska.food.controllers;

import com.example.praza_inzynierska.food.dto.FoodRequest;
import com.example.praza_inzynierska.food.dto.FoodResponse;
import com.example.praza_inzynierska.food.models.Food;
import com.example.praza_inzynierska.food.services.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping()
    public ResponseEntity<Void> addFood(@RequestBody FoodRequest model) {
        return foodService.addFood(model);
    }

    @GetMapping("/user/{id}/date/{date}")
    public ResponseEntity<List<Food>> fetchFoodByDate(@PathVariable Long id,
                                                      @PathVariable String date) {
        return foodService.fetchFoodByDate(id, date);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        return foodService.deleteFoodById(id);
    }

    @GetMapping("/available/{userId}")
    public ResponseEntity<List<FoodResponse>> fetchAvailableFood(@PathVariable Long userId) {
        return foodService.fetchAvailableFood(userId);
    }

    @GetMapping("/{name}")
    public ResponseEntity<FoodResponse> findFoodByName(@PathVariable("name") String name) {
        return foodService.findFoodByName(name);
    }

    @GetMapping("/user/{userId}/chart/{date}")
    public ResponseEntity<List<Food>> fetchChartFood(@PathVariable Long userId,
                                                     @PathVariable String date) {
        return foodService.fetchChartFood(userId, date);
    }
}
