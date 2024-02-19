package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.TrainingExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingExerciseRepository extends JpaRepository<TrainingExercise, Long> {
    List<TrainingExercise> findByTrainingIdAndName(Long trainingId, String name);
}
