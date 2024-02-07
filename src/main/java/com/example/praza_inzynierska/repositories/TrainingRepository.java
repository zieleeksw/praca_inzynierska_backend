package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByUserId(Long userId);
}
