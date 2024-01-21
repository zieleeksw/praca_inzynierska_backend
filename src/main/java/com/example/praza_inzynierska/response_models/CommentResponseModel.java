package com.example.praza_inzynierska.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseModel {

    private Long id;
    private Long authorId;
    private String username;
    private String content;
    private String timestamp;
}
