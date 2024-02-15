package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.BaseAppExercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseAppExercisesRepository extends JpaRepository<BaseAppExercises, Long> {
    BaseAppExercises findByName(String name);
}
