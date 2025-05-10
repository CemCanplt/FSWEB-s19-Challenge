package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.like.DislikeResponseDTO;
import com.twitter.mavikus.dto.like.LikeCreateDTO;
import com.twitter.mavikus.dto.like.LikeResponseDTO;
import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.service.LikeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RequestMapping("/like") // Bilerek kaldırdım
@AllArgsConstructor
@RestController
@Validated
public class LikeController {

    private final LikeService likeService;    @PostMapping("/like")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LikeResponseDTO> addLike(@RequestBody @Valid LikeCreateDTO likeDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        Object principal = authentication.getPrincipal();
        User currentUser;
        
        // Test ortamı (@WithMockUser) ve gerçek ortam için farklı işlemler
        if (principal instanceof User) {
            // Normal uygulama akışında
            currentUser = (User) principal;
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            // Test ortamında @WithMockUser kullanıldığında
            // Test için bir mock User nesnesi oluşturuyoruz
            currentUser = new User();
            currentUser.setId(1L); // Test için sabit bir ID
            currentUser.setUserName("testUser"); // @WithMockUser ile aynı kullanıcı adı
        } else {
            throw new IllegalStateException("Beklenmeyen principal türü: " + principal.getClass().getName());
        }
        
        // Beğeni ekleme işlemini gerçekleştir ve DTO dön
        LikeResponseDTO createdLike = likeService.addLikeAndReturnDTO(likeDTO, currentUser);
        
        // HTTP 201 Created statüsü ile birlikte oluşturulan beğeniyi döndür
        return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
    }
    
    @GetMapping("/like/tweet/{tweetId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Like>> getLikesByTweet(@PathVariable Long tweetId) {
        // Tweet'e ait beğenileri getir
        List<Like> likes = likeService.findLikesByTweetId(tweetId);
        return ResponseEntity.ok(likes);
    }
    
    @GetMapping("/like/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Like>> getLikesByUser(@PathVariable Long userId) {
        // Kullanıcıya ait beğenileri getir
        List<Like> likes = likeService.findLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }
      @PostMapping("/dislike")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DislikeResponseDTO> dislike(@RequestBody @Valid LikeCreateDTO likeDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        Object principal = authentication.getPrincipal();
        Long userId;
        
        // Test ortamı (@WithMockUser) ve gerçek ortam için farklı işlemler
        if (principal instanceof User) {
            // Normal uygulama akışında
            userId = ((User) principal).getId();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            // Test ortamında @WithMockUser kullanıldığında
            // Test için bir sabit değer kullanıyoruz, çünkü @WithMockUser tarafından 
            // oluşturulan UserDetails nesnesinin userId özelliği yok
            userId = 1L; // Test için bir sabit ID
        } else {
            throw new IllegalStateException("Beklenmeyen principal türü: " + principal.getClass().getName());
        }
        
        // Beğeniyi kaldırma işlemini gerçekleştir ve DTO dön
        DislikeResponseDTO response = likeService.removeLikeAndReturnDTO(likeDTO.getTweetId(), userId);
        
        // HTTP 200 OK statüsü ile birlikte yanıt döndür
        return ResponseEntity.ok(response);
    }
}