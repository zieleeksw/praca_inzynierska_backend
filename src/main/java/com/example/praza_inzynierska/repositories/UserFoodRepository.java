package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.UserFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFoodRepository extends JpaRepository<UserFood, Long> {

    List<UserFood> findByUserId(long userId);

    Optional<UserFood> findByProductName(String name);

    Optional<UserFood> findByProductNameAndUserId(String productName, long userId);
}
