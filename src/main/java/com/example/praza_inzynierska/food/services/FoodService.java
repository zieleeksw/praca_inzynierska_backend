package com.example.praza_inzynierska.food.services;

import com.example.praza_inzynierska.food.dto.FoodRequest;
import com.example.praza_inzynierska.food.dto.FoodResponse;
import com.example.praza_inzynierska.food.models.BaseAppFood;
import com.example.praza_inzynierska.food.models.Food;
import com.example.praza_inzynierska.food.models.UserFood;
import com.example.praza_inzynierska.food.repositories.BaseAppFoodRepository;
import com.example.praza_inzynierska.food.repositories.FoodRepository;
import com.example.praza_inzynierska.food.repositories.UserFoodRepository;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final BaseAppFoodRepository baseAppFoodRepository;
    private final UserFoodRepository userFoodRepository;

    public ResponseEntity<Void> addFood(FoodRequest model) {
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

    private Food getFood(User user, FoodRequest model) {
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

    public ResponseEntity<List<Food>> fetchChartFood(Long userId, String date) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Food> targetMonthFood = foodRepository.findByUserIdAndDateContaining(userId, date);

            List<Food> dailySums = targetMonthFood.stream()
                    .collect(Collectors.groupingBy(Food::getDate))
                    .entrySet().stream()
                    .map(entry -> {
                        List<Food> dayFoods = entry.getValue();
                        int sumKcal = dayFoods.stream().mapToInt(Food::getKcal).sum();
                        int sumProtein = dayFoods.stream().mapToInt(Food::getProteins).sum();
                        int sumFat = dayFoods.stream().mapToInt(Food::getFat).sum();
                        int sumCarbs = dayFoods.stream().mapToInt(Food::getCarbs).sum();

                        return Food.builder()
                                .date(entry.getKey())
                                .kcal(sumKcal)
                                .proteins(sumProtein)
                                .fat(sumFat)
                                .carbs(sumCarbs)
                                .build();
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(dailySums, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
