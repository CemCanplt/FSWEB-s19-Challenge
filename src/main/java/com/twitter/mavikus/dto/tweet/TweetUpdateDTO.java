package com.twitter.mavikus.dto.tweet;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetUpdateDTO {
    @NotBlank(message = "Tweet içeriği boş olamaz")
    private String content;
}