package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.retweet.RetweetConvertorDTO;
import com.twitter.mavikus.dto.retweet.RetweetCreateDTO;
import com.twitter.mavikus.dto.retweet.RetweetResponseDTO;
import com.twitter.mavikus.entity.Retweet;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.RetweetRepository;
import com.twitter.mavikus.repository.TweetRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;

    @Override
    public List<Retweet> findAll() {
        return retweetRepository.findAll();
    }

    @Override
    public Retweet findById(long id) {
        return retweetRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen retweet bulunamadı: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Retweet save(Retweet instanceOfRetweet) {
        return retweetRepository.save(instanceOfRetweet);
    }

    @Override
    public Retweet deleteById(long id) {
        Retweet retweet = findById(id);
        retweetRepository.deleteById(id);
        return retweet;
    }

    @Override
    @Transactional
    public Retweet createRetweet(RetweetCreateDTO retweetDTO, User user) {
        // Tweet'i ID'ye göre bul
        Tweet originalTweet = tweetRepository.findById(retweetDTO.getTweetId())
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen tweet bulunamadı: " + retweetDTO.getTweetId(),
                        HttpStatus.NOT_FOUND));
        
        // Kullanıcının bu tweet'i daha önce retweet edip etmediğini kontrol et
        Optional<Retweet> existingRetweet = retweetRepository.findByOriginalTweetIdAndUserId(retweetDTO.getTweetId(), user.getId());
        if (existingRetweet.isPresent()) {
            throw new MaviKusErrorException("Bu tweet'i zaten retweet ettiniz.", HttpStatus.CONFLICT);
        }
        
        // Yeni retweet nesnesi oluştur
        Retweet retweet = new Retweet();
        retweet.setOriginalTweet(originalTweet);
        retweet.setUser(user);
        
        try {
            // Retweet'i kaydet ve döndür
            return retweetRepository.save(retweet);
        } catch (DataIntegrityViolationException e) {
            // Veritabanı kısıtlaması exception'ı durumunda daha anlaşılır bir hata mesajı dön
            throw new MaviKusErrorException("Bu tweet'i zaten retweet ettiniz. Aynı tweet'i birden fazla kez retweet edemezsiniz.", 
                    HttpStatus.CONFLICT);
        }
    }
    
    @Override
    @Transactional
    public RetweetResponseDTO createRetweetAndReturnDTO(RetweetCreateDTO retweetDTO, User user) {
        // Mevcut createRetweet metodunu kullanarak retweet ekle
        Retweet createdRetweet = createRetweet(retweetDTO, user);
        
        // Entity'yi DTO'ya dönüştür
        return RetweetConvertorDTO.toResponseDTO(createdRetweet);
    }

    @Override
    public List<Retweet> findRetweetsByTweetId(Long tweetId) {
        // Tweet'in var olup olmadığını kontrol et
        if (!tweetRepository.existsById(tweetId)) {
            throw new MaviKusErrorException("Bu id ile eşleşen tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND);
        }
        
        // Tweet'in retweet'lerini döndür
        return retweetRepository.findByOriginalTweetId(tweetId);
    }

    @Override
    public List<Retweet> findRetweetsByUserId(Long userId) {
        return retweetRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Retweet deleteRetweetByOwner(Long retweetId, Long userId) {
        // Retweet'i ID'ye göre bul
        Retweet retweet = findById(retweetId);
        
        // Retweet'in sahibi bu kullanıcı mı kontrol et
        if (!retweet.getUser().getId().equals(userId)) {
            throw new MaviKusErrorException("Bu retweet'i silme yetkiniz yok! Retweet sadece sahibi tarafından silinebilir.", HttpStatus.FORBIDDEN);
        }
        
        // Retweet'i sil
        retweetRepository.deleteById(retweetId);
        
        // Silinen retweet'i döndür
        return retweet;
    }
    
    @Override
    @Transactional
    public RetweetResponseDTO deleteRetweetByOwnerAndReturnDTO(Long retweetId, Long userId) {
        // Retweet'i ID'ye göre bul
        Retweet retweet = findById(retweetId);
        
        // Retweet'in sahibi bu kullanıcı mı kontrol et
        if (!retweet.getUser().getId().equals(userId)) {
            throw new MaviKusErrorException("Bu retweet'i silme yetkiniz yok! Retweet sadece sahibi tarafından silinebilir.", HttpStatus.FORBIDDEN);
        }
        
        // Retweet bilgilerini sakla
        RetweetResponseDTO responseDTO = RetweetConvertorDTO.toResponseDTO(retweet);
        
        // Retweet'i sil
        retweetRepository.deleteById(retweetId);
        
        // Silinen retweet bilgilerini döndür
        return responseDTO;
    }
}
