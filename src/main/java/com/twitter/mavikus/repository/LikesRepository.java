package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Comments;
import com.twitter.mavikus.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
