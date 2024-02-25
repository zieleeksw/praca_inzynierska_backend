package com.example.praza_inzynierska.training.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExerciseRequest {
    private long userId;
    private String name;
}
