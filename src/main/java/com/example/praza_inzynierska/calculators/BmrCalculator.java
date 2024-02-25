package com.example.praza_inzynierska.calculators;

import com.example.praza_inzynierska.Constants;

public class BmrCalculator {

    private final String gender;
    private final String activityLevel;
    private final int height;
    private final double currentWeight;
    private final double targetWeight;
    private final int age;
    private final String dietStatus;

    public BmrCalculator(String gender, String activityLevel, int height, double currentWeight, double targetWeight, int age) {
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.height = height;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.age = age;
        this.dietStatus = calculateDietStatus();
    }

    public int calculate() {
        double bmr;
        bmr = getBasicBmr();
        bmr = getBmrWithActivityLevel(bmr);
        bmr = getBmrWithDietStatus(bmr);
        return (int) bmr;
    }

    public String getDietStatus() {
        return dietStatus;
    }

    private double getBasicBmr() {
        if (gender.equals("Male")) {
            return 66 + (13.7 * currentWeight) + (5 * height) - 6.8 * age;
        }
        return 655 + (9.6 * currentWeight) + (1.8 * height) - (4.7 * age);
    }

    private double getBmrWithActivityLevel(double bmr) {
        switch (activityLevel) {
            case "Very low" -> bmr = bmr * 1.22;
            case "Low" -> bmr = bmr * 1.375;
            case "Average" -> bmr = bmr * 1.55;
            case "Active" -> bmr = bmr * 1.725;
            case "Very active" -> bmr = bmr * 1.9;
        }
        return bmr;
    }

    private double getBmrWithDietStatus(double bmr) {
        if (dietStatus.equals("Bulking")) {
            return bmr + 7700 * Constants.WEIGHT_PER_WEEK / 7;
        } else if (dietStatus.equals("Reduction")) {
            return bmr - 7700 * Constants.WEIGHT_PER_WEEK / 7;
        }
        return bmr;
    }

    private String calculateDietStatus() {
        if (currentWeight == targetWeight) {
            return "Weight maintenance";
        } else if (currentWeight > targetWeight) {
            return "Reduction";
        }
        return "Bulking";
    }
}
