package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.tweet.TweetCreateDTO;
import com.twitter.mavikus.dto.tweet.TweetDeleteResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetResponseDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateDTO;
import com.twitter.mavikus.dto.tweet.TweetUpdateResponseDTO;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;

import java.util.List;
import java.util.Map;

public interface TweetService {
    // Buraya servis fonksiyonlarını yazabilirsin.

    // CRUD Operasyonları

    // Read: Tüm Tweet Entity'lerini getir
    List<Tweet> findAll();

    // Read: Belirli bir ID'ye sahip Tweet'i getir
    Tweet findById(long id);

    // Create / Update: Yeni bir Tweet kaydet veya var olanı güncelle
    Tweet save(Tweet instanceOfTweet);

    // Delete: Belirli bir ID'ye sahip Tweet öğesini sil
    Tweet deleteById(long id);

    // Update: Var olan Tweet'i güncelle
    Tweet update(long id, String text);

    // Yeni tweet oluşturma metodu
    Tweet createTweet(TweetCreateDTO tweetDTO, User user);

    // Tweet response map oluşturma metodu
    Map<String, Object> createTweetResponseMap(Tweet tweet, User user);

    // Kullanıcıya ait tweetleri getirme metodu
    List<Tweet> findTweetsByUserId(long userId);

    // Tweet detaylarını getirme metodu (DTO formatında)
    TweetResponseDTO getTweetWithDetails(long tweetId);

    // Tweet güncelleme metodu (tweetId, güncelleme DTO ve kullanıcı kimliği)
    Tweet updateTweet(Long tweetId, TweetUpdateDTO tweetUpdateDTO, Long currentUserId);

    // Kullanıcıya ait tweetleri DTO formatında getirme metodu
    List<TweetResponseDTO> findTweetDTOsByUserId(long userId);

    // Tweet güncelleme metodu (basitleştirilmiş DTO dönüşü)
    TweetUpdateResponseDTO updateTweetSimple(Long tweetId, TweetUpdateDTO tweetUpdateDTO, Long currentUserId);

    // Tweet silme metodu (DTO dönüşü)
    TweetDeleteResponseDTO deleteTweetAndReturnDTO(Long tweetId, Long currentUserId);
}
