package com.twitter.mavikus.controller;

import com.twitter.mavikus.entity.Tweets;
import com.twitter.mavikus.entity.Users;
import com.twitter.mavikus.service.TweetsService;
import com.twitter.mavikus.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tweet")
@AllArgsConstructor
@RestController
@Validated
public class TweetsController {

    private final TweetsService tweetsService;
    private final UsersService usersService;
    // Bu genelde bir service olur.

    @PostMapping
    public Tweets postTweet(@RequestBody Tweets tweet) {
        return tweetsService.save(tweet);
    }

    @GetMapping("/findById")
    public Tweets getTweetById(@RequestParam long tweetId) {
        // Yoldan gelen ID'yi al
        // tweetService'i kullanarak ID'ye göre tek bir tweet getir
        return tweetsService.findById(tweetId);
    }

    @GetMapping("/findByUserId") // /tweet/findByUserId şekline eşlenir
    public List<Tweets> getAllTweetsByUserId(@RequestParam long userId) { // Query parameter'dan userId'yi al
        // usersService kullanarak User'ı bul ve tweetlerini getir
        Users user = usersService.findById(userId);
        // User entity'sinde tweetler LAZY geliyorsa bu satır onları yükleyecektir.
        // Eğer user entity'de tweet listesi LAZY ve fetch join kullanmıyorsan
        // veya UserService'te tweetleri yükleyen özel bir metodun yoksa,
        // sadece user objesini döndürmek tweetleri getirmez.
        // user.getTweets() dediğinde veritabanına gidip tweetleri çekecektir.
        return user.getTweets().stream().toList();
    }

    @PutMapping("/{id}")
    public Tweets postTweet(@PathVariable Long id, @RequestBody String text) {
        return tweetsService.update(id, text);
    }

    @DeleteMapping("/{id}")
    public Tweets deleteTweet(@PathVariable Long id) {
        return tweetsService.deleteById(id);
    }


}
