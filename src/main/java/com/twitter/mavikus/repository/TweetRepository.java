package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
