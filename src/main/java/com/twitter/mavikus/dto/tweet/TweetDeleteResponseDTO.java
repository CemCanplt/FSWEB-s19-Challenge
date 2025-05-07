package com.twitter.mavikus.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tweet silme yanıtı için DTO sınıfı
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetDeleteResponseDTO {
    private Long id;
    private String deletedContent;
    private boolean success;
}