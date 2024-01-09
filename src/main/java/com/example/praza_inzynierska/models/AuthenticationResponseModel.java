package com.example.praza_inzynierska.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponseModel {

    private long id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String token;
    private NutritionConfig userNutritionConfig;
}
