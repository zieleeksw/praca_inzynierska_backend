package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.BaseAppFood;
import com.example.praza_inzynierska.repositories.BaseAppFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseAppFoodService {

    private final BaseAppFoodRepository baseAppFoodRepository;

    public ResponseEntity<List<BaseAppFood>> fetchAllBaseAppFood() {
        try {
            List<BaseAppFood> baseAppFood = baseAppFoodRepository.findAll();
            return new ResponseEntity<>(baseAppFood, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseAppFood> findFoodByName(String name) {
        try {
            BaseAppFood food = baseAppFoodRepository.findByProductName(name);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
