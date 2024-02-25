package com.example.praza_inzynierska.user.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DimensionsRequest {
    private long userId;
    private String date;
    private int arm;
    private int chest;
    private int waist;
    private int leg;
    private int calf;
}
