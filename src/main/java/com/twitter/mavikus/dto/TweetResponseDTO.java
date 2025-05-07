package com.twitter.mavikus.dto;

import com.twitter.mavikus.entity.Comment;
import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Retweet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponseDTO {
    private Long id;
    private String content;
    private Instant createdAt;
    private UserDTO user;
    private List<Like> likes;
    private List<Comment> comments;
    private List<Retweet> retweets;
    
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