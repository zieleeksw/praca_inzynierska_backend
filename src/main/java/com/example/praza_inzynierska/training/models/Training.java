package com.example.praza_inzynierska.training.models;

import com.example.praza_inzynierska.user.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Training {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    @OneToMany(mappedBy = "training")
    private List<TrainingExercise> exercises;
}
