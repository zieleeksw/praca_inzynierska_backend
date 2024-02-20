package com.example.praza_inzynierska.controllers;

import com.example.praza_inzynierska.models.BodyDimensions;
import com.example.praza_inzynierska.request.models.DimensionsRequest;
import com.example.praza_inzynierska.services.DimensionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dimensions")
@RequiredArgsConstructor
public class BodyDimensionsController {

    private final DimensionsService dimensionsService;


    @GetMapping("/{userId}")
    public ResponseEntity<List<BodyDimensions>> fetchDimensions(@PathVariable Long userId) {
        return dimensionsService.fetchDimensions(userId);
    }

    @PostMapping()
    public ResponseEntity<Void> addDimensions(@RequestBody DimensionsRequest model) {
        return dimensionsService.addDimensions(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimensions(@PathVariable Long id) {
        return dimensionsService.deleteDimensionsById(id);
    }

}
