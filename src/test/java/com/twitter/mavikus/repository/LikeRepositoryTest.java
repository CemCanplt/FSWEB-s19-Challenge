package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private User testUser;
    private Tweet testTweet;
    private Like testLike;
    
    @BeforeEach
    void setUp() {
        
        // Test için gerekli olan kullanıcı oluşturalım
        testUser = new User();
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        entityManager.persist(testUser);
        
        // Test için gerekli olan tweet oluşturalım
        testTweet = new Tweet();
        testTweet.setContent("Test tweet içeriği");
        testTweet.setUser(testUser);
        entityManager.persist(testTweet);
        
        // Test için gerekli olan beğeni oluşturalım
        testLike = new Like();
        testLike.setTweet(testTweet);
        testLike.setUser(testUser);
        entityManager.persist(testLike);
        
        // EntityManager'daki değişiklikleri veritabanına yansıtalım
        entityManager.flush();
    }
    
    @DisplayName("Belirli bir tweet ve kullanıcıya ait beğeni kaydını bulabilmeli")
    @Test
    void findByTweetIdAndUserId() {
        // When - Eylemi gerçekleştirelim
        Optional<Like> foundLike = likeRepository.findByTweetIdAndUserId(testTweet.getId(), testUser.getId());
        
        // Then - Sonuçları doğrulayalım
        assertThat(foundLike).isPresent();
        assertThat(foundLike.get().getTweet().getId()).isEqualTo(testTweet.getId());
        assertThat(foundLike.get().getUser().getId()).isEqualTo(testUser.getId());
    }

    @DisplayName("Belirli bir tweet'e ait tüm beğenileri bulabilmeli")
    @Test
    void findByTweetId() {
        // When - Eylemi gerçekleştirelim
        List<Like> likes = likeRepository.findByTweetId(testTweet.getId());
        
        // Then - Sonuçları doğrulayalım
        assertThat(likes).isNotEmpty();
        assertThat(likes).hasSize(1);
        assertThat(likes.get(0).getTweet().getId()).isEqualTo(testTweet.getId());
    }

    @DisplayName("Belirli bir kullanıcının yaptığı tüm beğenileri bulabilmeli")
    @Test
    void findByUserId() {
        // When - Eylemi gerçekleştirelim
        List<Like> likes = likeRepository.findByUserId(testUser.getId());
        
        // Then - Sonuçları doğrulayalım
        assertThat(likes).isNotEmpty();
        assertThat(likes).hasSize(1);
        assertThat(likes.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }
}