package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.TweetCreateDTO;
import com.twitter.mavikus.dto.TweetUpdateDTO;
import com.twitter.mavikus.entity.Tweets;
import com.twitter.mavikus.service.TweetsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tweet")
@AllArgsConstructor
@RestController
@Validated
public class TweetsController {

    private final TweetsService tweetsService;

    @PostMapping
    public Tweets postTweet(@RequestBody @Valid TweetCreateDTO tweetDTO) {
        // Service katmanına iş mantığını devret, hata yönetimini service yapar
        return tweetsService.createTweet(tweetDTO);
    }

    @GetMapping("/findById")
    public ResponseEntity<Tweets> getTweetById(@RequestParam long tweetId) {
        // Service katmanına iş mantığını devret
        Tweets tweet = tweetsService.getTweetWithDetails(tweetId);
        return ResponseEntity.ok(tweet);
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<Tweets>> getAllTweetsByUserId(@RequestParam long userId) {
        // Service katmanına iş mantığını devret
        List<Tweets> tweets = tweetsService.findTweetsByUserId(userId);
        return ResponseEntity.ok(tweets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweets> updateTweet(@PathVariable Long id, @RequestBody @Valid TweetUpdateDTO tweetUpdateDTO) {
        // Service katmanına iş mantığını devret
        Tweets updatedTweet = tweetsService.updateTweet(id, tweetUpdateDTO);
        return ResponseEntity.ok(updatedTweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tweets> deleteTweet(@PathVariable Long id, @RequestParam Long userId) {
        // Service katmanına iş mantığını devret
        Tweets deletedTweet = tweetsService.deleteTweetByOwner(id, userId);
        return ResponseEntity.ok(deletedTweet);
    }
}
