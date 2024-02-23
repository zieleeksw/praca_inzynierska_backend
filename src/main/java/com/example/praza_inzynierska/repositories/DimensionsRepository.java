package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.BodyDimensions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DimensionsRepository extends JpaRepository<BodyDimensions, Long> {
    List<BodyDimensions> findByUserId(Long userId);
}
