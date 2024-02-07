package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.BaseAppExercises;
import com.example.praza_inzynierska.repositories.BaseAppExercisesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseAppExercisesService {

    private final BaseAppExercisesRepository baseAppExercisesRepository;

    public ResponseEntity<List<BaseAppExercises>> fetchAllBaseAppExercises() {
        try {
            List<BaseAppExercises> baseAppExercises = baseAppExercisesRepository.findAll();
            return new ResponseEntity<>(baseAppExercises, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
