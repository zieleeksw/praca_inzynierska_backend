package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.BodyDimensions;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.repositories.DimensionsRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.DimensionsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DimensionsService {

    private final DimensionsRepository dimensionsRepository;
    private final UserRepository userRepository;

    public ResponseEntity<List<BodyDimensions>> fetchDimensions(Long userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<BodyDimensions> bodyDimensions = dimensionsRepository.findByUserId(userId);
            return new ResponseEntity<>(bodyDimensions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> addDimensions(DimensionsRequest model) {
        try {
            Optional<User> userOptional = userRepository.findById(model.getUserId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            User user = userOptional.get();
            BodyDimensions bodyDimensions = getBodyDimensions(model, user);
            dimensionsRepository.save(bodyDimensions);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private BodyDimensions getBodyDimensions(DimensionsRequest model, User user) {
        return BodyDimensions.builder()
                .user(user)
                .date(model.getDate())
                .arm(model.getArm())
                .waist(model.getWaist())
                .chest(model.getChest())
                .leg(model.getLeg())
                .calf(model.getCalf())
                .build();
    }

    public ResponseEntity<Void> deleteDimensionsById(Long commentId) {
        try {
            Optional<BodyDimensions> optional = dimensionsRepository.findById(commentId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            dimensionsRepository.deleteById(commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
