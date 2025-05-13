package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.tweet.TweetCreateDTO;
import com.twitter.mavikus.dto.tweet.TweetDeleteResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetSimpleDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateResponseDTO;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.service.TweetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/tweet")
@AllArgsConstructor
@RestController
@Validated
public class TweetController {

    private final TweetService tweetService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> postTweet(@RequestBody @Valid TweetCreateDTO tweetDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Tweet'i oluştur
        Tweet createdTweet = tweetService.createTweet(tweetDTO, currentUser);
        
        // Service katmanında response map'i oluştur
        Map<String, Object> response = tweetService.createTweetResponseMap(createdTweet, currentUser);
        
        // HTTP 201 Created statüsü ile yanıt döndür
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/findById")
    public ResponseEntity<TweetResponseDTO> getTweetById(@RequestParam long tweetId) {
        // Service katmanına iş mantığını devret
        TweetResponseDTO tweet = tweetService.getTweetWithDetails(tweetId);
        return ResponseEntity.ok(tweet);
    }    /**
     * Yeni eklenen endpoint - Basit DTO formatında dönüş yapar ve auth gerektirmez (public)
     * Bu metod sadece tweet id ve içeriğini döndürür, gereksiz veri trafiği önler
     */
    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetSimpleDTO>> getAllTweetsByUserId(@RequestParam long userId) {
        // Service katmanına iş mantığını devret
        List<TweetSimpleDTO> tweetSimpleDTOs = tweetService.findTweetSimpleDTOsByUserId(userId);
        return ResponseEntity.ok(tweetSimpleDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetUpdateResponseDTO> updateTweet(@PathVariable Long id, 
                                            @RequestBody @Valid TweetUpdateDTO tweetUpdateDTO, 
                                            Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Service katmanına iş mantığını devret, kullanıcı ID'sini de geçiriyoruz
        // ve sadece gerekli bilgileri içeren DTO döndürüyoruz
        TweetUpdateResponseDTO response = tweetService.updateTweetSimple(id, tweetUpdateDTO, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TweetDeleteResponseDTO> deleteTweet(@PathVariable Long id, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Service katmanına iş mantığını devret, kullanıcı ID'sini de geçiriyoruz
        // ve sadece gerekli bilgileri içeren DTO döndürüyoruz
        TweetDeleteResponseDTO response = tweetService.deleteTweetAndReturnDTO(id, currentUser.getId());
        return ResponseEntity.ok(response);
    }
}
