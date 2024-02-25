package com.example.praza_inzynierska.food.repositories;

import com.example.praza_inzynierska.food.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByDateAndUserId(String date, Long userId);

    List<Food> findByUserIdAndDateContaining(Long userId, String date);
}
