package com.twitter.mavikus.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Tweet güncelleme yanıtı için DTO sınıfı
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetUpdateResponseDTO {
    private Long id;
    private String updatedContent;
    private Instant updatedAt;
    private boolean success;
}