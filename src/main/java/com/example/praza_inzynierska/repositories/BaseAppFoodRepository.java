package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.BaseAppFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseAppFoodRepository extends JpaRepository<BaseAppFood, Long> {

    Optional<BaseAppFood> findByProductName(String name);
}
