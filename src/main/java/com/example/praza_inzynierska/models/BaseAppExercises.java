package com.example.praza_inzynierska.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseAppExercises {

    @Id
    @GeneratedValue
    private long id;
    String name;
}
