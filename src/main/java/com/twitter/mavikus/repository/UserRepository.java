package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // userName alanına göre sorgulama yapar
    // User entity'sindeki property ismine göre metod isimlendirilmelidir
    Optional<User> findByUserName(String userName);
    
    // Alternatif olarak doğrudan SQL sorgusuyla da tanımlayabiliriz
    /*
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    Optional<User> findUserByUserName(@Param("username") String username);
    */
}
