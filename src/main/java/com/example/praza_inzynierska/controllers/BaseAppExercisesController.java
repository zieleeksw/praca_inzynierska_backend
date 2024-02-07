package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.BaseAppExercises;
import com.example.praza_inzynierska.services.BaseAppExercisesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/base_app_exercises")
public class BaseAppExercisesController {

    private final BaseAppExercisesService baseAppExercisesService;

    @GetMapping
    public ResponseEntity<List<BaseAppExercises>> fetchAllBaseAppExercises() {
        return baseAppExercisesService.fetchAllBaseAppExercises();
    }
}
