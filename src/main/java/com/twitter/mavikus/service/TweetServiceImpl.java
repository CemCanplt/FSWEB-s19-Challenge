package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.TweetCreateDTO;
import com.twitter.mavikus.dto.TweetResponseDTO;
import com.twitter.mavikus.dto.TweetUpdateDTO;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.TweetRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;

    @Override
    @Transactional
    public List<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    @Override
    @Transactional
    public Tweet findById(long id) {
        return tweetRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Tweet save(Tweet instanceOfTweet) {
        return tweetRepository.save(instanceOfTweet);
    }

    @Override
    @Transactional
    public Tweet deleteById(long id) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));
        tweetRepository.deleteById(id);
        return tweet;
    }

    @Override
    @Transactional
    public Tweet update(long id, String text) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));

        tweet.setContent(text);

        return tweetRepository.save(tweet);
    }

    @Override
    @Transactional
    public Tweet createTweet(TweetCreateDTO tweetDTO, User user) {
        // Tweet içeriği kontrolü (Validation anotasyonları ile zaten kontrol ediliyor ama ekstra kontrol)
        if (tweetDTO.getContent() == null || tweetDTO.getContent().trim().isEmpty()) {
            throw new MaviKusErrorException("Tweet içeriği boş olamaz", HttpStatus.BAD_REQUEST);
        }

        // Yeni tweet nesnesi oluştur
        Tweet tweet = new Tweet();
        tweet.setContent(tweetDTO.getContent());
        tweet.setUser(user);
        
        // Tweet'i kaydet ve döndür
        return tweetRepository.save(tweet);
    }

    @Override
    @Transactional
    public List<Tweet> findTweetsByUserId(long userId) {
        try {
            // Kullanıcıyı bul - hata durumunda usersService zaten exception fırlatacak
            User user = userService.findById(userId);
            
            // Kullanıcının tweetlerini getir ve createdAt tarihine göre en yeniden en eskiye sırala
            return user.getTweets().stream()
                    .sorted(Comparator.comparing(Tweet::getCreatedAt).reversed())
                    .toList();
        } catch (Exception e) {
            // Eğer kullanıcı bulunamama hatası dışında bir hata varsa
            if (!(e instanceof MaviKusErrorException)) {
                throw new MaviKusErrorException("Kullanıcının tweetleri getirilirken bir hata oluştu: " + e.getMessage(), 
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // MaviKusErrorException hatası doğrudan yeniden fırlat
            throw e;
        }
    }

    @Override
    @Transactional
    public TweetResponseDTO getTweetWithDetails(long tweetId) {
        // Tweet'i ID'ye göre veritabanından getir
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND));
        
        // Eğer tweet'e ait ilişkiler LAZY yükleme yapıyorsa, bunları yükle
        Hibernate.initialize(tweet.getLikes());
        Hibernate.initialize(tweet.getComments());
        Hibernate.initialize(tweet.getRetweets());
        
        // Tweet'ten TweetResponseDTO'ya dönüştürme
        TweetResponseDTO.UserDTO userDTO = TweetResponseDTO.UserDTO.builder()
                .id(tweet.getUser().getId())
                .userName(tweet.getUser().getUsername())
                .email(tweet.getUser().getEmail())
                .createdAt(tweet.getUser().getCreatedAt())
                .build();
        
        return TweetResponseDTO.builder()
                .id(tweet.getId())
                .content(tweet.getContent())
                .createdAt(tweet.getCreatedAt())
                .user(userDTO)
                .likes(tweet.getLikes())
                .comments(tweet.getComments())
                .retweets(tweet.getRetweets())
                .build();
    }

    @Override
    @Transactional
    public Tweet updateTweet(Long tweetId, TweetUpdateDTO tweetUpdateDTO, Long currentUserId) {
        // Tweet'i ID'ye göre bul
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND));
        
        // Tweet'i güncelleyen kullanıcının tweet sahibi olup olmadığını kontrol et
        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new MaviKusErrorException("Bu tweeti güncelleme yetkiniz yok! Tweet sadece sahibi tarafından güncellenebilir.", 
                                           HttpStatus.FORBIDDEN);
        }
        
        // Tweet içeriğini güncelle
        tweet.setContent(tweetUpdateDTO.getContent());
        
        // updatedAt alanı @UpdateTimestamp sayesinde otomatik güncellenecek
        
        // Güncellenmiş tweet'i kaydet ve döndür
        return tweetRepository.save(tweet);
    }

    @Override
    @Transactional
    public Tweet deleteTweetByOwner(Long tweetId, Long userId) {
        // Tweet'i ID'ye göre bul
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND));
        
        // Tweet'in sahibi bu kullanıcı mı kontrol et
        if (!tweet.getUser().getId().equals(userId)) {
            throw new MaviKusErrorException("Bu tweeti silme yetkiniz yok! Tweet sadece sahibi tarafından silinebilir.", HttpStatus.FORBIDDEN);
        }
        
        // Tweet'i sil
        tweetRepository.deleteById(tweetId);
        
        // Silinen tweet'i döndür
        return tweet;
    }
}
