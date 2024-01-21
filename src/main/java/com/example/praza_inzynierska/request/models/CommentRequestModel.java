package com.example.praza_inzynierska.request.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestModel {

    private long authorId;
    private long postId;
    private String content;
}