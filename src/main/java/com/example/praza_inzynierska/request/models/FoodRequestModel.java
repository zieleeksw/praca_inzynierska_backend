package com.example.praza_inzynierska.request.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodRequestModel {

    private  long authorId;
    private  String meal;
    private  String date;
    private  String grams;
    private  String productName;
    private  int kcal;
    private  int fat;
    private  int carbs;
    private  int proteins;

}
