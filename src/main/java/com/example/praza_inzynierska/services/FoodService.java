package com.example.praza_inzynierska.services;

import com.example.praza_inzynierska.models.BaseAppFood;
import com.example.praza_inzynierska.models.Food;
import com.example.praza_inzynierska.models.User;
import com.example.praza_inzynierska.models.UserFood;
import com.example.praza_inzynierska.repositories.BaseAppFoodRepository;
import com.example.praza_inzynierska.repositories.FoodRepository;
import com.example.praza_inzynierska.repositories.UserFoodRepository;
import com.example.praza_inzynierska.repositories.UserRepository;
import com.example.praza_inzynierska.request.models.FoodRequestModel;
import com.example.praza_inzynierska.response_models.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final BaseAppFoodRepository baseAppFoodRepository;
    private final UserFoodRepository userFoodRepository;

    public ResponseEntity<Void> addFood(FoodRequestModel model) {
        try {
            Optional<User> userOptional = userRepository.findById(model.getAuthorId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            User user = userOptional.get();
            Food food = getFood(user, model);
            foodRepository.save(food);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Food getFood(User user, FoodRequestModel model) {
        return Food.builder()
                .user(user)
                .meal(model.getMeal())
                .date(model.getDate())
                .grams(model.getGrams())
                .productName(model.getProductName())
                .kcal(model.getKcal())
                .fat(model.getFat())
                .carbs(model.getCarbs())
                .proteins(model.getProteins())
                .build();
    }

    public ResponseEntity<List<Food>> fetchFoodByDate(Long id, String date) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Food> food = foodRepository.findByDateAndUserId(date, id);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteFoodById(Long id) {
        try {
            Optional<Food> optional = foodRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            foodRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<FoodResponse>> fetchAvailableFood(long userId) {
        try {
            List<FoodResponse> baseAppFood = baseAppFoodRepository.findAll().stream().map(FoodResponse::new).toList();
            List<FoodResponse> result = new ArrayList<>(baseAppFood);
            List<FoodResponse> userFood = userFoodRepository.findByUserId(userId).stream().map(FoodResponse::new).toList();
            result.addAll(userFood);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodResponse> findFoodByName(String name) {
        try {
            Optional<BaseAppFood> baseAppFoodOptional = baseAppFoodRepository.findByProductName(name);
            if (baseAppFoodOptional.isPresent()) {
                return new ResponseEntity<>(new FoodResponse(baseAppFoodOptional.get()), HttpStatus.OK);
            } else {
                Optional<UserFood> userFoodOptional = userFoodRepository.findByProductName(name);
                if (userFoodOptional.isPresent()) {
                    return new ResponseEntity<>(new FoodResponse(userFoodOptional.get()), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
