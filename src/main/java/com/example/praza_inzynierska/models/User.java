package com.example.praza_inzynierska.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "username", unique = true)
    @NonNull
    private String username;
    @NonNull
    @Column(name = "email", unique = true)
    private String email;
    @NonNull
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NutritionConfig userNutritionConfig;
}
