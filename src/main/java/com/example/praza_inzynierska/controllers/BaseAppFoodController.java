package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.BaseAppFood;
import com.example.praza_inzynierska.services.BaseAppFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/base_app_food")
public class BaseAppFoodController {

    private final BaseAppFoodService baseAppFoodService;

    @GetMapping
    public ResponseEntity<List<BaseAppFood>> fetchAllBaseAppFood() {
        return baseAppFoodService.fetchAllBaseAppFood();
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<BaseAppFood> findFoodByName(@PathVariable("name") String name) {
//        return baseAppFoodService.findFoodByName(name);
//    }
}
