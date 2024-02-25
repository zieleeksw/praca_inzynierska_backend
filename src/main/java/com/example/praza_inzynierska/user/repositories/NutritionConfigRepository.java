package com.example.praza_inzynierska.user.repositories;

import com.example.praza_inzynierska.user.models.NutritionConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionConfigRepository extends JpaRepository<NutritionConfig, Long> {

    NutritionConfig findByUserId(Long userId);
}
