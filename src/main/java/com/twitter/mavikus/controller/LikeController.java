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

    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<LikeResponseDTO> addLike(@RequestBody @Valid LikeCreateDTO likeDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Beğeni ekleme işlemini gerçekleştir ve DTO dön
        LikeResponseDTO createdLike = likeService.addLikeAndReturnDTO(likeDTO, currentUser);
        
        // HTTP 201 Created statüsü ile birlikte oluşturulan beğeniyi döndür
        return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
    }
    
    @GetMapping("/like/tweet/{tweetId}")
    public ResponseEntity<List<Like>> getLikesByTweet(@PathVariable Long tweetId) {
        // Tweet'e ait beğenileri getir
        List<Like> likes = likeService.findLikesByTweetId(tweetId);
        return ResponseEntity.ok(likes);
    }
    
    @GetMapping("/like/user/{userId}")
    public ResponseEntity<List<Like>> getLikesByUser(@PathVariable Long userId) {
        // Kullanıcıya ait beğenileri getir
        List<Like> likes = likeService.findLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }
    
    @PostMapping("/dislike")
    public ResponseEntity<DislikeResponseDTO> dislike(@RequestBody @Valid LikeCreateDTO likeDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Beğeniyi kaldırma işlemini gerçekleştir ve DTO dön
        DislikeResponseDTO response = likeService.removeLikeAndReturnDTO(likeDTO.getTweetId(), currentUser.getId());
        
        // HTTP 200 OK statüsü ile birlikte yanıt döndür
        return ResponseEntity.ok(response);
    }
}