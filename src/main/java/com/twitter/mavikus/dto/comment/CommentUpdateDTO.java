package com.twitter.mavikus.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDTO {
    @NotBlank(message = "Yorum içeriği boş olamaz")
    private String commentText;
}