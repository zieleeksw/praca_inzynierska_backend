package com.example.praza_inzynierska.request.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingRequestModel {

    private String name;
    private String date;
}
