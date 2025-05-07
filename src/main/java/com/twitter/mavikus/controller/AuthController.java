package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.LoginDTO;
import com.twitter.mavikus.dto.RegisterResponseDTO;
import com.twitter.mavikus.dto.RegisterUserDTO;
import com.twitter.mavikus.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// @RequestMapping("/auth")
@AllArgsConstructor
@RestController
@Validated
public class AuthController {

    private final AuthenticationService authenticationService;
    // Bu genelde bir service olur.

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterUserDTO registerUser) {
        RegisterResponseDTO response = authenticationService.register(
                registerUser.userName(), 
                registerUser.email(), 
                registerUser.password()
        );
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginDTO loginDTO) {
        // Kullanıcıyı doğrula
        Authentication authentication = authenticationService.login(loginDTO.getUserName(), loginDTO.getPassword());
        
        // Başarılı ise kullanıcı bilgilerini içeren bir yanıt döndür
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("authenticated", authentication.isAuthenticated());
        
        return ResponseEntity.ok(response);
    }
}
