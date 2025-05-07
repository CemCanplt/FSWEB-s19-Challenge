package com.twitter.mavikus.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tweet beğeni silme yanıtı için DTO sınıfı
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DislikeResponseDTO {
    private Long tweetId;
    private String tweetContent;
    private boolean success;
    private String message;
}