package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // Tweet ID ve User ID'ye göre beğeni bul
    Optional<Like> findByTweetIdAndUserId(Long tweetId, Long userId);
    
    // Tweet ID'sine göre tüm beğenileri getir
    List<Like> findByTweetId(Long tweetId);
    
    // User ID'sine göre tüm beğenileri getir
    List<Like> findByUserId(Long userId);
}
