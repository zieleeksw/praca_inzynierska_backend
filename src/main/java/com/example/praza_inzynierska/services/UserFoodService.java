package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.BaseAppFood;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.models.UserFood;
import com.example.praza_inzynierska.repositories.BaseAppFoodRepository;
import com.example.praza_inzynierska.repositories.UserFoodRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.UserFoodRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFoodService {

    private final UserRepository userRepository;
    private final BaseAppFoodRepository baseAppFoodRepository;
    private final UserFoodRepository userFoodRepository;

    public ResponseEntity<List<UserFood>> fetchUserFood(Long userId) {
        try {
            List<UserFood> userFood = userFoodRepository.findByUserId(userId);
            return new ResponseEntity<>(userFood, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> addUserFood(UserFoodRequest model) {
        try {
            Optional<User> user = userRepository.findById(model.getUserId());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<UserFood> userFoodOptional = userFoodRepository.findByProductNameAndUserId(model.getProductName(), model.getUserId());
            if (userFoodOptional.isPresent()) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            Optional<BaseAppFood> baseAppFoodOptional = baseAppFoodRepository.findByProductName(model.getProductName());
            if (baseAppFoodOptional.isPresent()) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            UserFood userFood = getUserFood(user.get(), model);
            userFoodRepository.save(userFood);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserFood getUserFood(User user, UserFoodRequest model) {
        return UserFood.builder()
                .user(user)
                .productName(model.getProductName())
                .kcal(model.getKcal())
                .fat(model.getFat())
                .carbs(model.getCarbs())
                .proteins(model.getProteins())
                .build();
    }

    public ResponseEntity<Void> deleteUserFoodByName(Long userId, String foodName) {
        try {
            Optional<UserFood> optional = userFoodRepository.findByProductNameAndUserId(foodName, userId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            userFoodRepository.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
