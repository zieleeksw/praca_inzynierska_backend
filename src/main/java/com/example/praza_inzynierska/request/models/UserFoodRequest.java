package com.example.praza_inzynierska.request.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFoodRequest {

    private long userId;
    private String productName;
    private int kcal;
    private int fat;
    private int carbs;
    private int proteins;
}
