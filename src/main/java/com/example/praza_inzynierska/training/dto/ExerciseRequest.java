package com.example.praza_inzynierska.training.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequest {

    private Long userId;
    private String name;
    private String date;
    private int repetition;
    private double weight;
}
