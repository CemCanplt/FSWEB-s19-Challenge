package com.twitter.mavikus.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Like yanıtı için DTO sınıfı
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long tweetId;
    private String tweetContent;
}