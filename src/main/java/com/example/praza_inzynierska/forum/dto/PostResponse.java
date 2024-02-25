package com.example.praza_inzynierska.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private Long authorId;
    private String author;
    private String content;
    private String timestamp;
    private List<Long> followers;
}
