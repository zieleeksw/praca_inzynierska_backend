package com.example.praza_inzynierska.request.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequestModel {

    private Long userId;
    private String name;
    private String date;
    private int repetition;
    private double weight;
}
