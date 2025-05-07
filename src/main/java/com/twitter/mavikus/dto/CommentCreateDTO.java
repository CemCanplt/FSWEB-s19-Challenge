package com.twitter.mavikus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDTO {
    @NotNull(message = "Tweet ID boş olamaz")
    private Long tweetId;
    
    @NotBlank(message = "Yorum içeriği boş olamaz")
    private String commentText;
}