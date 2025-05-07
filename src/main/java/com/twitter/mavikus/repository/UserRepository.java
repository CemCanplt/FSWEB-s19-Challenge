package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // List<[TUR]> [FONKSIYON_ADI]([PARAMETRELER]);
    @Query(value = "SELECT u FROM users u WHERE u.user_name = :userName", nativeQuery = true)
    Optional<User> findUserByUserName(String userName);

}
