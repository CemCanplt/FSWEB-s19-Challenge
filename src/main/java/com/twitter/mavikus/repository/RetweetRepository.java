package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    // Tweet ID ve User ID'ye göre retweet bul
    Optional<Retweet> findByOriginalTweetIdAndUserId(Long originalTweetId, Long userId);
    
    // Tweet ID'sine göre tüm retweet'leri getir
    List<Retweet> findByOriginalTweetId(Long originalTweetId);
    
    // User ID'sine göre tüm retweet'leri getir
    List<Retweet> findByUserId(Long userId);
}
