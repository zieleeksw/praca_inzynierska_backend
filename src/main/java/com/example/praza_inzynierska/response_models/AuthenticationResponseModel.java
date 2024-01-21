package com.example.praza_inzynierska.response_models;

import com.example.praza_inzynierska.models.NutritionConfig;
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
