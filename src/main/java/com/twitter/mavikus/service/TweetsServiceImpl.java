package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.TweetCreateDTO;
import com.twitter.mavikus.dto.TweetUpdateDTO;
import com.twitter.mavikus.entity.Tweets;
import com.twitter.mavikus.entity.Users;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.TweetsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TweetsServiceImpl implements TweetsService {

    private final TweetsRepository tweetsRepository;
    private final UsersService usersService;

    @Override
    @Transactional
    public List<Tweets> findAll() {
        return tweetsRepository.findAll();
    }

    @Override
    @Transactional
    public Tweets findById(long id) {
        return tweetsRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Tweets save(Tweets instanceOfTweets) {
        return tweetsRepository.save(instanceOfTweets);
    }

    @Override
    @Transactional
    public Tweets deleteById(long id) {
        Tweets tweet = tweetsRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));
        tweetsRepository.deleteById(id);
        return tweet;
    }

    @Override
    @Transactional
    public Tweets update(long id, String text) {
        Tweets tweet = tweetsRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));

        tweet.setContent(text);

        return tweetsRepository.save(tweet);
    }
    
    @Override
    @Transactional
    public Tweets createTweet(TweetCreateDTO tweetDTO) {
        // Kullanıcı var mı kontrol et - hata durumunda usersService zaten exception fırlatacak
        Users user = usersService.findById(tweetDTO.getUserId());
        
        // Tweet içeriği kontrolü (Validation anotasyonları ile zaten kontrol ediliyor ama ekstra kontrol)
        if (tweetDTO.getContent() == null || tweetDTO.getContent().trim().isEmpty()) {
            throw new MaviKusErrorException("Tweet içeriği boş olamaz", HttpStatus.BAD_REQUEST);
        }

        // Yeni tweet nesnesi oluştur
        Tweets tweet = new Tweets();
        tweet.setContent(tweetDTO.getContent());
        tweet.setUser(user);
        
        // Tweet'i kaydet ve döndür
        return tweetsRepository.save(tweet);
    }

    // Belki güncellerim, garanti değil
    @Override
    @Transactional
    public List<Tweets> findTweetsByUserId(long userId) {
        try {
            // Kullanıcıyı bul - hata durumunda usersService zaten exception fırlatacak
            Users user = usersService.findById(userId);
            
            // Kullanıcının tweetlerini getir
            return user.getTweets().stream().toList();
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
    public Tweets getTweetWithDetails(long tweetId) {
        // Tweet'i ID'ye göre veritabanından getir
        Tweets tweet = tweetsRepository.findById(tweetId)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND));
        
        // Eğer tweet'e ait ilişkiler LAZY yükleme yapıyorsa, bunları yükle
        // Hibernate.initialize(tweet.getLikes());
        // Hibernate.initialize(tweet.getComments());
        
        return tweet;
        
        /* 
         * Not: Eğer tweet entity'niz içinde likes, comments, retweets gibi ilişkiler varsa
         * ve bunlar LAZY olarak yükleniyorsa, yukarıdaki gibi initialize etmelisiniz.
         * 
         * Ya da Repository'de özel bir sorgu yazarak ilişkili nesneleri JOIN ile getirebilirsiniz:
         * Örnek: tweetsRepository.findTweetWithDetailsById(tweetId);
         */
    }

    @Override
    @Transactional
    public Tweets updateTweet(Long id, TweetUpdateDTO tweetUpdateDTO) {
        // Tweet'i ID'ye göre bul
        Tweets tweet = tweetsRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: " + id, HttpStatus.NOT_FOUND));
        
        // Not: Gerçek bir uygulamada, giriş yapmış kullanıcının tweet sahibi olup olmadığını kontrol etmelisiniz.
        // Örnek: Bu tweet giriş yapmış kullanıcıya ait mi?
        // if (!tweet.getUser().getId().equals(loggedInUser.getId())) {
        //    throw new MaviKusErrorException("Bu tweeti güncelleme yetkiniz yok!", HttpStatus.FORBIDDEN);
        // }
        
        // Tweet içeriğini güncelle
        tweet.setContent(tweetUpdateDTO.getContent());
        
        // Güncellenmiş tweet'i kaydet ve döndür
        return tweetsRepository.save(tweet);
    }

    @Override
    @Transactional
    public Tweets deleteTweetByOwner(Long tweetId, Long userId) {
        // Tweet'i ID'ye göre bul
        Tweets tweet = tweetsRepository.findById(tweetId)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: " + tweetId, HttpStatus.NOT_FOUND));
        
        // Tweet'in sahibi bu kullanıcı mı kontrol et
        if (!tweet.getUser().getId().equals(userId)) {
            throw new MaviKusErrorException("Bu tweeti silme yetkiniz yok! Tweet sadece sahibi tarafından silinebilir.", HttpStatus.FORBIDDEN);
        }
        
        // Tweet'i sil
        tweetsRepository.deleteById(tweetId);
        
        // Silinen tweet'i döndür
        return tweet;
    }
}
