package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.TweetCreateDTO;
import com.twitter.mavikus.dto.TweetResponseDTO;
import com.twitter.mavikus.dto.TweetUpdateDTO;
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
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<Tweet>> getAllTweetsByUserId(@RequestParam long userId) {
        // Service katmanına iş mantığını devret
        List<Tweet> tweets = tweetService.findTweetsByUserId(userId);
        return ResponseEntity.ok(tweets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long id, 
                                            @RequestBody @Valid TweetUpdateDTO tweetUpdateDTO, 
                                            Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Service katmanına iş mantığını devret, kullanıcı ID'sini de geçiriyoruz
        Tweet updatedTweet = tweetService.updateTweet(id, tweetUpdateDTO, currentUser.getId());
        return ResponseEntity.ok(updatedTweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tweet> deleteTweet(@PathVariable Long id, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Service katmanına iş mantığını devret, kullanıcı ID'sini de geçiriyoruz
        Tweet deletedTweet = tweetService.deleteTweetByOwner(id, currentUser.getId());
        return ResponseEntity.ok(deletedTweet);
    }
}
