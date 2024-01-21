package com.example.praza_inzynierska.request.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestModel {

    private long authorId;
    private String content;
}
