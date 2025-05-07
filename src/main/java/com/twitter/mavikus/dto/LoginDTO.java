package com.twitter.mavikus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String userName;
    
    @NotBlank(message = "Şifre boş olamaz")
    private String password;
}