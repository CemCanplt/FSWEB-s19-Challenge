package com.twitter.mavikus.dto.like;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeCreateDTO {
    @NotNull(message = "Tweet ID boş olamaz")
    private Long tweetId;
}