package com.example.praza_inzynierska.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue()
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String meal;
    private String date;
    private String grams;
    private String productName;
    private int kcal;
    private int fat;
    private int carbs;
    private int proteins;
}
