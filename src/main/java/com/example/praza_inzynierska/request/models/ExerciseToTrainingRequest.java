package com.example.praza_inzynierska.request.models;

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