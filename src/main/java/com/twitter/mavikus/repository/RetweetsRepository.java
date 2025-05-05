package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Comments;
import com.twitter.mavikus.entity.Retweets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetsRepository extends JpaRepository<Retweets, Long> {
}
