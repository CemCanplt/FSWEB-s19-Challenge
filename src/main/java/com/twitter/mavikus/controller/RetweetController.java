package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.retweet.RetweetCreateDTO;
import com.twitter.mavikus.dto.retweet.RetweetResponseDTO;
import com.twitter.mavikus.entity.Retweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.service.RetweetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/retweet")
@AllArgsConstructor
@RestController
@Validated
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping
    public ResponseEntity<RetweetResponseDTO> createRetweet(@RequestBody @Valid RetweetCreateDTO retweetDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Retweet işlemini gerçekleştir ve DTO dön
        RetweetResponseDTO createdRetweet = retweetService.createRetweetAndReturnDTO(retweetDTO, currentUser);
        
        // HTTP 201 Created statüsü ile birlikte oluşturulan retweet'i döndür
        return new ResponseEntity<>(createdRetweet, HttpStatus.CREATED);
    }
    
    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<Retweet>> getRetweetsByTweet(@PathVariable Long tweetId) {
        // Tweet'e ait retweet'leri getir
        List<Retweet> retweets = retweetService.findRetweetsByTweetId(tweetId);
        return ResponseEntity.ok(retweets);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Retweet>> getRetweetsByUser(@PathVariable Long userId) {
        // Kullanıcıya ait retweet'leri getir
        List<Retweet> retweets = retweetService.findRetweetsByUserId(userId);
        return ResponseEntity.ok(retweets);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<RetweetResponseDTO> deleteRetweet(@PathVariable Long id, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Retweet silme işlemini gerçekleştir ve DTO dön
        RetweetResponseDTO deletedRetweet = retweetService.deleteRetweetByOwnerAndReturnDTO(id, currentUser.getId());
        
        // HTTP 200 OK statüsü ile birlikte silinen retweet bilgilerini döndür
        return ResponseEntity.ok(deletedRetweet);
    }
}
