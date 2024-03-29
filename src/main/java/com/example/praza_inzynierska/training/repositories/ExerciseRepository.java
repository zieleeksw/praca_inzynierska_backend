package com.example.praza_inzynierska.training.repositories;

import com.example.praza_inzynierska.training.models.Exercise;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByDateAndUserIdAndName(String date, long userId, String name);

    List<Exercise> findByDateAndUserId(String date, long userId);

    List<Exercise> findByUserIdAndNameAndDateContaining(Long userId, String name, String datePart);

    @Transactional
    @Modifying
    @Query("DELETE FROM Exercise e WHERE e.date = :date AND e.name = :name")
    void deleteByDateAndName(String date, String name);
}
