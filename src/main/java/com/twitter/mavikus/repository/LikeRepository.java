package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
