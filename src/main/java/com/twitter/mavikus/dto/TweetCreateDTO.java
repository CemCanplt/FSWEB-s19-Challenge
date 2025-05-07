package com.twitter.mavikus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetCreateDTO {
    @NotBlank(message = "Tweet içeriği boş olamaz")
    private String content;
}