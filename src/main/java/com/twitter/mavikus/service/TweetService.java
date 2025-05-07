package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.TweetCreateDTO;
import com.twitter.mavikus.dto.TweetResponseDTO;
import com.twitter.mavikus.dto.TweetUpdateDTO;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;

import java.util.List;
import java.util.Map;

public interface TweetService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Tweets Entity'lerini getir
    List<Tweet> findAll();

    // Read: Belirli bir ID'ye sahip Tweets'u getir
    // Optional kullanılabilir, Tweets bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Tweets ID'sinin Long olduğunu varsayıyoruz
    Tweet findById(long id);

    // Create / Update: Yeni bir Tweets kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Tweet save(Tweet instanceOfTweet);

    // Delete: Belirli bir ID'ye sahip Tweets öğesini sil
    // Tweets ID'sinin Long olduğunu varsayıyoruz
    Tweet deleteById(long id);

    Tweet update(long id, String text);
    
    // Yeni eklenen metodlar
    
    List<Tweet> findTweetsByUserId(long userId);
    
    // Bir tweet'in tüm detaylarını getir - artık TweetResponseDTO döndürüyor
    TweetResponseDTO getTweetWithDetails(long tweetId);
    
    // Tweet güncelleme için yeni metod (DTO ve kullanıcı kimliği kullanarak)
    Tweet updateTweet(Long tweetId, TweetUpdateDTO tweetUpdateDTO, Long currentUserId);
    
    // Tweet silme - sadece sahibi silebilir (currentUserId ile)
    Tweet deleteTweetByOwner(Long tweetId, Long currentUserId);
    
    // Kullanıcı kimliğini doğrudan alan yeni metod
    Tweet createTweet(TweetCreateDTO tweetDTO, User user);
    
    // Tweet bilgilerinden response map oluşturan yeni metod
    Map<String, Object> createTweetResponseMap(Tweet tweet, User user);
}
