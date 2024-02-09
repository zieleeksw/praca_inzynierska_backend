package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.BaseAppFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseAppFoodRepository extends JpaRepository<BaseAppFood, Long> {

    BaseAppFood findByProductName(String name);
}
