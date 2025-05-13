package com.twitter.mavikus.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Tweet'e yapılan etkileşimleri (like, comment, retweet) basit bir şekilde temsil eden DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InteractionDTO {
    private Long id;         // Etkileşimin ID'si
    private Long userId;     // Etkileşimi yapan kullanıcının ID'si
    private String userName; // Etkileşimi yapan kullanıcının adı
    private Instant createdAt; // Etkileşimin oluşturulma zamanı
    
    // Comment için ek alan
    private String commentText;
}
