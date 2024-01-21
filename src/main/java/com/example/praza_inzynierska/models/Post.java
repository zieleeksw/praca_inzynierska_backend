package com.example.praza_inzynierska.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue()
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    private String content;
    private String timestamp;
    @ElementCollection
    private List<Long> followers;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
