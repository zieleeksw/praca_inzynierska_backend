package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.Food;
import com.example.praza_inzynierska.request.models.FoodRequestModel;
import com.example.praza_inzynierska.services.FoodService;
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
    public ResponseEntity<Void> addFood(@RequestBody FoodRequestModel model) {
        return foodService.addFood(model);
    }

    @GetMapping("/user/{id}/date/{date}")
    public ResponseEntity<List<Food>> fetchCommentsFromPost(@PathVariable Long id,
                                                            @PathVariable String date) {
        return foodService.fetchFoodByDate(id, date);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        return foodService.deleteFoodById(id);
    }
}