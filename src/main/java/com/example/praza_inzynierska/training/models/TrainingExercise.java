package com.example.praza_inzynierska.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingExercise {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int repetition;
    private double weight;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;
}
