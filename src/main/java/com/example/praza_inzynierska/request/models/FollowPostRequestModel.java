package com.example.praza_inzynierska.request.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowPostRequestModel {

    private long userId;
    private long postId;
}
