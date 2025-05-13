package com.twitter.mavikus.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Tweet yanıtı için DTO sınıfı
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponseDTO {
    private Long id;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private UserDTO user;
    private List<InteractionDTO> likes;
    private List<InteractionDTO> comments;
    private List<InteractionDTO> retweets;

    /**
     * Tweet yanıtında kullanıcı bilgilerini taşıyan DTO sınıfı
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        private Long id;
        private String userName;
        private String email;
        private Instant createdAt;
        // Şifre burada yer almıyor
    }
}