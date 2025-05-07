package com.twitter.mavikus.dto.retweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Retweet yanıtı için DTO sınıfı
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetweetResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long tweetId;
    private String tweetContent;
}