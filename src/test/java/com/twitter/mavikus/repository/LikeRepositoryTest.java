package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest
// @AutoConfigureTestDatabase(replace = Replace.ANY) // Gömülü veritabanı kullanımını etkinleştir
@SpringBootTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TweetRepository tweetRepository;

    private User testUser;
    private Tweet testTweet;
    private Like testLike;
     
    @BeforeEach
    void setUp() {
        // Test için gerekli olan kullanıcı oluşturalım ve kaydedelim
        testUser = new User();
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        // User nesnesinde Role koleksiyonu varsa boş bir koleksiyon oluştur
        if (testUser.getRoles() == null) {
            testUser.setRoles(new HashSet<>());
        }
        testUser = userRepository.save(testUser);
        
        // Test için gerekli olan tweet oluşturalım ve kaydedelim
        testTweet = new Tweet();
        testTweet.setContent("Test tweet içeriği");
        testTweet.setUser(testUser);
        testTweet = tweetRepository.save(testTweet);
        
        // Test için gerekli olan beğeni oluşturalım ve kaydedelim
        testLike = new Like();
        testLike.setTweet(testTweet);
        testLike.setUser(testUser);
        testLike = likeRepository.save(testLike);
    }
    
    @AfterEach
    void tearDown() {
        // Test sonrası verileri temizle
        likeRepository.deleteAll();
        tweetRepository.deleteAll();
        userRepository.deleteAll();
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