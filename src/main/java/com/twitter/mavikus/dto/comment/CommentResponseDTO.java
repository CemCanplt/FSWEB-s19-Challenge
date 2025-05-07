package com.twitter.mavikus.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String commentText;
    private Long userId;
    private String userName;
    private Long tweetId;
    private Instant createdAt;
    private Instant updatedAt;
}