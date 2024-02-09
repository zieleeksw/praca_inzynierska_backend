package com.example.praza_inzynierska.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseAppFood {

    @Id
    @GeneratedValue
    private long id;
    private String productName;
    private int kcal;
    private int fat;
    private int carbs;
    private int proteins;
}