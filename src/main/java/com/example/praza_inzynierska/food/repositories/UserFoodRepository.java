package com.example.praza_inzynierska.food.repositories;

import com.example.praza_inzynierska.food.models.UserFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFoodRepository extends JpaRepository<UserFood, Long> {

    List<UserFood> findByUserId(long userId);

    Optional<UserFood> findByProductName(String name);

    Optional<UserFood> findByProductNameAndUserId(String productName, long userId);
}
