package com.example.praza_inzynierska.models;

import com.example.praza_inzynierska.Constants;
import com.example.praza_inzynierska.calculators.BmrCalculator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "nutrition_config")
public class NutritionConfig {

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @Id
    @JsonIgnore
    private long id;
    private String gender;
    private LocalDate dob;
    private String activityLevel;
    private int height;
    private double currentWeight;
    private double targetWeight;
    @Transient
    private String dietStatus;
    @Transient
    private int caloricNeeds;
    @Transient
    private int carbNeeds;
    @Transient
    private int fatNeeds;
    @Transient
    int proteinNeeds;
    @Transient
    private LocalDate dietFinish;

    public NutritionConfig(User user, long id, String gender, LocalDate dob, String activityLevel,
                           int height, double currentWeight, double targetWeight) {
        this.user = user;
        this.id = id;
        this.gender = gender;
        this.dob = dob;
        this.activityLevel = activityLevel;
        this.height = height;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
    }

    public int getCaloricNeeds() {
        BmrCalculator calculator = new BmrCalculator(gender, activityLevel, height, currentWeight, targetWeight, getAge());
        return calculator.calculate();
    }

    public String getDietStatus() {
        BmrCalculator calculator = new BmrCalculator(gender, activityLevel, height, currentWeight, targetWeight, getAge());
        calculator.calculate();
        return calculator.getDietStatus();
    }

    public LocalDate getDietFinish() {
        double weightDifference = Math.abs(currentWeight - targetWeight);
        if (weightDifference == 0) {
            return LocalDate.now();
        }
        int weeksToReachTarget = (int) Math.ceil(weightDifference / Constants.WEIGHT_PER_WEEK);
        return LocalDate.now().plusWeeks(weeksToReachTarget);
    }

    private int getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public int getFatNeeds() {
        int caloricNeeds = getCaloricNeeds();
        double fatPercentage = 0.25;
        return (int) (caloricNeeds * fatPercentage / Constants.FAT_CALORIES_PER_GRAM);
    }

    public int getProteinNeeds() {
        int caloricNeeds = getCaloricNeeds();
        double proteinPercentage = 0.20;
        return (int) (caloricNeeds * proteinPercentage / Constants.PROTEIN_CALORIES_PER_GRAM);
    }

    public int getCarbNeeds() {
        int caloricNeeds = getCaloricNeeds();
        double remainingCalories = caloricNeeds - (fatNeeds * Constants.FAT_CALORIES_PER_GRAM + proteinNeeds * Constants.PROTEIN_CALORIES_PER_GRAM);
        double carbPercentage = 0.55;
        return (int) (remainingCalories * carbPercentage / Constants.CARB_CALORIES_PER_GRAM);
    }
}
