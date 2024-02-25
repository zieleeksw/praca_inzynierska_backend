package com.example.praza_inzynierska.response_models;

import com.example.praza_inzynierska.models.BaseAppFood;
import com.example.praza_inzynierska.models.UserFood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponse {

    private String productName;
    private int kcal;
    private int fat;
    private int carbs;
    private int proteins;

    public FoodResponse(BaseAppFood baseAppFood) {
        this.productName = baseAppFood.getProductName();
        this.kcal = baseAppFood.getKcal();
        this.fat = baseAppFood.getFat();
        this.carbs = baseAppFood.getCarbs();
        this.proteins = baseAppFood.getProteins();
    }

    public FoodResponse(UserFood userFood) {
        this.productName = userFood.getProductName();
        this.kcal = userFood.getKcal();
        this.fat = userFood.getFat();
        this.carbs = userFood.getCarbs();
        this.proteins = userFood.getProteins();
    }
}
