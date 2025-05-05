package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Comments;
import com.twitter.mavikus.entity.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetsRepository extends JpaRepository<Tweets, Long> {
}
