package com.example.praza_inzynierska.authentication.dto;

import com.example.praza_inzynierska.user.models.NutritionConfig;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    private long id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String token;
    private NutritionConfig userNutritionConfig;
}
