package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByDate(String date);
}
