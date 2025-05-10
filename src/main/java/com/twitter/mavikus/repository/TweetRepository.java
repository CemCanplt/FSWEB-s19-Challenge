package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    // User ID'sine göre tweetleri getir
    List<Tweet> findByUserId(Long userId);
}
