package com.example.praza_inzynierska.forum.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

    private long authorId;
    private String content;
}
