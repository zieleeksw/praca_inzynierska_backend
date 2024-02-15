package com.example.praza_inzynierska.request.models;


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
