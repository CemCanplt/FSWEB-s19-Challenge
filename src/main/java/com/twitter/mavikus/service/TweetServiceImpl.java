package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.tweet.TweetCreateDTO;
import com.twitter.mavikus.dto.tweet.TweetConvertorDTO;
import com.twitter.mavikus.dto.tweet.TweetDeleteResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetSimpleDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateResponseDTO;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.TweetRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Tweet findById(long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen tweet bulunamadı: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public List<Tweet> findTweetsByUserId(long userId) {
        // Kullanıcının var olup olmadığını kontrol et
        userService.findById(userId); // Bu metot kullanıcı bulamazsa MaviKusErrorException fırlatır
        
        // Kullanıcının tweetlerini getir ve oluşturulma tarihine göre tersten sırala (en yeniler önce)
        return tweetRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Tweet::getCreatedAt).reversed())
                .collect(Collectors.toList());
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
    public Map<String, Object> createTweetResponseMap(Tweet tweet, User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", tweet.getId());
        response.put("content", tweet.getContent());
        response.put("createdAt", tweet.getCreatedAt());
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("userName", user.getUsername());
        userMap.put("email", user.getEmail());
        
        response.put("user", userMap);
        return response;
    }

    @Override
    @Transactional
    public TweetResponseDTO getTweetWithDetails(long tweetId) {
        Tweet tweet = findById(tweetId);
        
        // Lazy loaded koleksiyonları yükle
        Hibernate.initialize(tweet.getLikes());
        Hibernate.initialize(tweet.getComments());
        Hibernate.initialize(tweet.getRetweets());
        
        // TweetResponseDTO dönüştürme metodunu kullan
        return TweetConvertorDTO.toResponseDTO(tweet);
    }

    @Override
    @Transactional
    public TweetUpdateResponseDTO updateTweetSimple(Long tweetId, TweetUpdateDTO tweetUpdateDTO, Long currentUserId) {
        // Tweet'i güncelle
        Tweet tweet = findById(tweetId);
        
        // Tweet'i güncelleyen kullanıcının tweet sahibi olup olmadığını kontrol et
        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new MaviKusErrorException("Bu tweet'i güncelleme yetkiniz yok! Tweet sadece sahibi tarafından güncellenebilir.", 
                                          HttpStatus.FORBIDDEN);
        }
        
        // Tweet içeriğini kontrol et
        if (tweetUpdateDTO.getContent() == null || tweetUpdateDTO.getContent().trim().isEmpty()) {
            throw new MaviKusErrorException("Tweet içeriği boş olamaz", HttpStatus.BAD_REQUEST);
        }
        
        // Tweet'i güncelle
        tweet.setContent(tweetUpdateDTO.getContent());
        tweet.setUpdatedAt(Instant.now()); // Güncelleme zamanını ayarla
        
        // Güncellenmiş tweet'i kaydet
        Tweet updatedTweet = tweetRepository.save(tweet);
        
        // Güncellenmiş tweet'ten TweetUpdateResponseDTO oluştur
        return TweetConvertorDTO.toUpdateResponseDTO(updatedTweet);
    }

    @Override
    @Transactional
    public TweetDeleteResponseDTO deleteTweetAndReturnDTO(Long tweetId, Long currentUserId) {
        // Tweet'i ID'ye göre bul
        Tweet tweet = findById(tweetId);
        
        // Tweet'i silen kullanıcının tweet sahibi olup olmadığını kontrol et
        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new MaviKusErrorException("Bu tweet'i silme yetkiniz yok! Tweet sadece sahibi tarafından silinebilir.", 
                                          HttpStatus.FORBIDDEN);
        }
        
        // Silme işleminden önce tweet bilgilerini sakla
        TweetDeleteResponseDTO responseDTO = TweetConvertorDTO.toDeleteResponseDTO(tweet);
        
        // Tweet'i sil
        tweetRepository.deleteById(tweetId);
        
        // Silme bilgilerini içeren DTO'yu döndür
        return responseDTO;
    }

    @Override
    @Transactional
    public List<TweetSimpleDTO> findTweetSimpleDTOsByUserId(long userId) {
        // Kullanıcının tweetlerini getir
        List<Tweet> tweets = findTweetsByUserId(userId);
        
        // Tweet'leri basit DTO'lara dönüştür
        return tweets.stream()
                .map(TweetConvertorDTO::toSimpleDTO)
                .collect(Collectors.toList());
    }
}
