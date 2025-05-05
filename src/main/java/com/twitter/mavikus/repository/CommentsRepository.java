package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
