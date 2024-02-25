package com.example.praza_inzynierska.training.repositories;

import com.example.praza_inzynierska.training.models.UserExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {

    List<UserExercise> findByUserId(long userId);

    UserExercise findByName(String name);
}
