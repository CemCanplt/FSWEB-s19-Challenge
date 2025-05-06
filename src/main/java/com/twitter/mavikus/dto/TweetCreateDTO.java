package com.twitter.mavikus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetCreateDTO {
    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long userId;
    
    @NotBlank(message = "Tweet içeriği boş olamaz")
    private String content;
}