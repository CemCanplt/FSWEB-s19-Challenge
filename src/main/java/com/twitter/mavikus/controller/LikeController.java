package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.LikeCreateDTO;
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

@RequestMapping("/like")
@AllArgsConstructor
@RestController
@Validated
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Like> addLike(@RequestBody @Valid LikeCreateDTO likeDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Beğeni ekleme işlemini gerçekleştir
        Like createdLike = likeService.addLike(likeDTO, currentUser);
        
        // HTTP 201 Created statüsü ile birlikte oluşturulan beğeniyi döndür
        return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
    }
    
    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<Like>> getLikesByTweet(@PathVariable Long tweetId) {
        // Tweet'e ait beğenileri getir
        List<Like> likes = likeService.findLikesByTweetId(tweetId);
        return ResponseEntity.ok(likes);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> getLikesByUser(@PathVariable Long userId) {
        // Kullanıcıya ait beğenileri getir
        List<Like> likes = likeService.findLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }
    
    /*
    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long tweetId, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Beğeniyi kaldırma işlemini gerçekleştir
        likeService.removeLike(tweetId, currentUser.getId());
        
        // HTTP 204 No Content statüsü döndür
        return ResponseEntity.noContent().build();
    }
    */
    
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(@RequestBody @Valid LikeCreateDTO likeDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Beğeniyi kaldırma işlemini gerçekleştir
        likeService.removeLike(likeDTO.getTweetId(), currentUser.getId());
        
        // HTTP 200 OK statüsü döndür
        return ResponseEntity.ok().build();
    }
}