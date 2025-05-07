package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.auth.RegisterResponseDTO;
import com.twitter.mavikus.entity.Role;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.RoleRepository;
import com.twitter.mavikus.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public RegisterResponseDTO register(String username, String email, String password) {
        try {
            // İstek parametrelerini kontrol et
            if (username == null || username.trim().isEmpty()) {
                return new RegisterResponseDTO(
                        null, null, null, null, false, 
                        "Kullanıcı adı boş olamaz!"
                );
            }
            
            if (email == null || email.trim().isEmpty()) {
                return new RegisterResponseDTO(
                        null, null, null, null, false, 
                        "E-mail adresi boş olamaz!"
                );
            }
            
            if (password == null || password.trim().isEmpty()) {
                return new RegisterResponseDTO(
                        null, null, null, null, false, 
                        "Şifre boş olamaz!"
                );
            }
            
            // Email veya kullanıcı adı ile daha önce kayıt olunmuş mu kontrol et
            try {
                if (userRepository.findByUserName(username).isPresent()) {
                    return new RegisterResponseDTO(
                            null, null, null, null, false, 
                            "Bu kullanıcı adı zaten kullanılıyor."
                    );
                }
            } catch (Exception e) {
                // Repository sorgu hatası
                System.err.println("Veritabanı sorgusu hatası: " + e.getMessage());
                return new RegisterResponseDTO(
                        null, null, null, null, false, 
                        "Kullanıcı doğrulama sırasında bir veritabanı hatası oluştu."
                );
            }
            
            String encodedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER");
            if (userRole == null) {
                return new RegisterResponseDTO(
                        null, null, null, null, false, 
                        "Kullanıcı rolü bulunamadı. Veritabanındaki roller kontrol edilmelidir."
                );
            }

            // Kullanıcıyı oluştur
            User user = new User();

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(encodedPassword);
            user.setRoles(roles);

            // Kullanıcıyı veritabanına kaydet
            User savedUser = userRepository.save(user);
            
            // Başarılı yanıt oluştur
            return new RegisterResponseDTO(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail(),
                    savedUser.getCreatedAt(),
                    true,
                    "Kullanıcı başarıyla kaydedildi."
            );
        } catch (ConstraintViolationException e) {
            // Doğrulama hatalarını detaylı şekilde döndür
            String violations = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            
            return new RegisterResponseDTO(
                    null, null, null, null, false, 
                    "Doğrulama hataları: " + violations
            );
        } catch (MaviKusErrorException e) {
            return new RegisterResponseDTO(
                    null, null, null, null, false, 
                    e.getMessage()
            );
        } catch (Exception e) {
            // Hata detaylarını logla
            System.err.println("Kullanıcı kaydı sırasında beklenmeyen hata: " + e.getMessage());
            e.printStackTrace();
            
            // Genel hata mesajı döndür
            return new RegisterResponseDTO(
                    null, null, null, null, false, 
                    "Kullanıcı kaydı sırasında bir hata oluştu: " + e.getMessage()
            );
        }
    }
    
    public Authentication login(String username, String password) {
        try {
            // Spring Security kimlik doğrulama
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            
            // Kimlik doğrulama başarılıysa, security context'e kaydet
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return authentication;
        } catch (AuthenticationException e) {
            throw new MaviKusErrorException("Kullanıcı adı veya şifre hatalı", HttpStatus.UNAUTHORIZED);
        }
    }
}
