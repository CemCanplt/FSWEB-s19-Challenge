package com.twitter.mavikus.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tweet listelemek için basit DTO sınıfı
 * Sadece temel tweet bilgilerini içerir
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetSimpleDTO {
    private Long id;
    private String content;
}
