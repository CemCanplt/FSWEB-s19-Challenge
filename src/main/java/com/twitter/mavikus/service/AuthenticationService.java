package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.RegisterResponseDTO;
import com.twitter.mavikus.entity.Role;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.RoleRepository;
import com.twitter.mavikus.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthenticationService {

    // Üye ve Rol Repo'ları genelde burada olur.
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public RegisterResponseDTO register(String username, String email, String password) {
        try {
            // Email veya kullanıcı adı ile daha önce kayıt olunmuş mu kontrol et
            if (userRepository.findUserByUserName(username).isPresent()) {
                return new RegisterResponseDTO(
                        null, null, null, null, false, 
                        "Bu kullanıcı adı zaten kullanılıyor."
                );
            }
            
            String encodedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER");

            // Burada kullanıcıyı kaydetme işlemi yapılır.
            User user = new User();

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(encodedPassword);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            
            // Güvenli bilgileri içeren yanıt döndür
            return new RegisterResponseDTO(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail(),
                    savedUser.getCreatedAt(),
                    true,
                    "Kullanıcı başarıyla kaydedildi."
            );
        } catch (Exception e) {
            return new RegisterResponseDTO(
                    null, null, null, null, false, 
                    "Kullanıcı kaydı sırasında bir hata oluştu: " + e.getMessage()
            );
        }
    }
    
    public Authentication login(String username, String password) {
        try {
            // Spring Security'nin kimlik doğrulama mekanizmasını kullanarak kimlik doğrulama yapın
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            
            // Kimlik doğrulama başarılıysa, security context'e kaydedin
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return authentication;
        } catch (AuthenticationException e) {
            // Kimlik doğrulama başarısız olursa hata fırlatın
            throw new MaviKusErrorException("Kullanıcı adı veya şifre hatalı", HttpStatus.UNAUTHORIZED);
        }
    }
}
