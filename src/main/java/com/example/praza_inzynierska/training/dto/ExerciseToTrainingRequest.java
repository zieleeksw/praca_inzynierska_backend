package com.example.praza_inzynierska.training.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseToTrainingRequest {
    private Long trainingId;
    private String name;
    private int repetition;
    private double weight;

}