package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
}
