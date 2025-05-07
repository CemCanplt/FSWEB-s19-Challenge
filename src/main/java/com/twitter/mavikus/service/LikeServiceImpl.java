package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.like.DislikeResponseDTO;
import com.twitter.mavikus.dto.like.LikeCreateDTO;
import com.twitter.mavikus.dto.like.LikeConvertorDTO;
import com.twitter.mavikus.dto.like.LikeResponseDTO;
import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.LikeRepository;
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
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;

    @Override
    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    @Override
    public Like findById(long id) {
        return likeRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen beğeni bulunamadı: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Like save(Like instanceOfLike) {
        return likeRepository.save(instanceOfLike);
    }

    @Override
    public Like deleteById(long id) {
        Like like = findById(id);
        likeRepository.deleteById(id);
        return like;
    }

    @Override
    @Transactional
    public Like addLike(LikeCreateDTO likeDTO, User currentUser) {
        // Tweet'i ID'ye göre bul
        Tweet tweet = tweetRepository.findById(likeDTO.getTweetId())
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen tweet bulunamadı: " + likeDTO.getTweetId(), 
                        HttpStatus.NOT_FOUND));
        
        // Kullanıcının bu tweet'i daha önce beğenip beğenmediğini kontrol et
        Optional<Like> existingLike = likeRepository.findByTweetIdAndUserId(likeDTO.getTweetId(), currentUser.getId());
        if (existingLike.isPresent()) {
            throw new MaviKusErrorException("Bu tweet'i zaten beğendiniz.", HttpStatus.CONFLICT);
        }
        
        // Yeni beğeni nesnesi oluştur
        Like like = new Like();
        like.setTweet(tweet);
        like.setUser(currentUser);
        
        try {
            // Beğeniyi kaydet ve döndür
            return likeRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            // Veritabanı kısıtlaması exception'ı durumunda daha anlaşılır bir hata mesajı dön
            throw new MaviKusErrorException("Bu tweet'i zaten beğendiniz. Aynı tweet'i birden fazla kez beğenemezsiniz.", 
                    HttpStatus.CONFLICT);
        }
    }
    
    @Override
    @Transactional
    public LikeResponseDTO addLikeAndReturnDTO(LikeCreateDTO likeDTO, User currentUser) {
        // Mevcut addLike metodunu kullanarak beğeni ekleyin
        Like createdLike = addLike(likeDTO, currentUser);
        
        // Entity'yi DTO'ya dönüştür
        return LikeConvertorDTO.toResponseDTO(createdLike);
    }

    @Override
    public List<Like> findLikesByTweetId(long tweetId) {
        // Tweet'in var olup olmadığını kontrol et
        if (!tweetRepository.existsById(tweetId)) {
            throw new MaviKusErrorException("Bu id ile eşleşen tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND);
        }
        
        // Tweet'in beğenilerini döndür
        return likeRepository.findByTweetId(tweetId);
    }

    @Override
    public List<Like> findLikesByUserId(long userId) {
        return likeRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional
    public DislikeResponseDTO removeLikeAndReturnDTO(long tweetId, long userId) {
        // Beğeninin var olup olmadığını kontrol et
        Optional<Like> existingLike = likeRepository.findByTweetIdAndUserId(tweetId, userId);
        if (!existingLike.isPresent()) {
            throw new MaviKusErrorException("Bu tweet için bir beğeniniz bulunmamaktadır.", HttpStatus.NOT_FOUND);
        }
        
        // Tweet bilgilerini sakla
        Tweet tweet = existingLike.get().getTweet();
        
        // Beğeniyi sil
        likeRepository.deleteById(existingLike.get().getId());
        
        // DislikeResponseDTO oluştur ve döndür
        return LikeConvertorDTO.toDislikeResponseDTO(tweet);
    }
}
