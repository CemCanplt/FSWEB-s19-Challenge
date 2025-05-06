package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.TweetCreateDTO;
import com.twitter.mavikus.dto.TweetUpdateDTO;
import com.twitter.mavikus.entity.Tweets;

import java.util.List;

public interface TweetsService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Tweets Entity'lerini getir
    List<Tweets> findAll();

    // Read: Belirli bir ID'ye sahip Tweets'u getir
    // Optional kullanılabilir, Tweets bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Tweets ID'sinin Long olduğunu varsayıyoruz
    Tweets findById(long id);

    // Create / Update: Yeni bir Tweets kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Tweets save(Tweets instanceOfTweets);

    // Delete: Belirli bir ID'ye sahip Tweets öğesini sil
    // Tweets ID'sinin Long olduğunu varsayıyoruz
    Tweets deleteById(long id);

    Tweets update(long id, String text);
    
    // Yeni eklenen metodlar
    Tweets createTweet(TweetCreateDTO tweetDTO);
    
    List<Tweets> findTweetsByUserId(long userId);
    
    // Bir tweet'in tüm detaylarını getir
    Tweets getTweetWithDetails(long tweetId);
    
    // Tweet güncelleme için yeni metod (DTO kullanarak)
    Tweets updateTweet(Long id, TweetUpdateDTO tweetUpdateDTO);
    
    // Tweet silme - sadece sahibi silebilir
    Tweets deleteTweetByOwner(Long tweetId, Long userId);
}
