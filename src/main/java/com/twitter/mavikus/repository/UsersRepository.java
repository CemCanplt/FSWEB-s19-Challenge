package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
