package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.RetweetCreateDTO;
import com.twitter.mavikus.entity.Retweet;
import com.twitter.mavikus.entity.User;

import java.util.List;

public interface RetweetService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Retweets Entity'lerini getir
    List<Retweet> findAll();

    // Read: Belirli bir ID'ye sahip Retweets'u getir
    // Optional kullanılabilir, Retweets bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Retweets ID'sinin Long olduğunu varsayıyoruz
    Retweet findById(long id);

    // Create / Update: Yeni bir Retweets kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Retweet save(Retweet instanceOfRetweet);

    // Delete: Belirli bir ID'ye sahip Retweets öğesini sil
    // Retweets ID'sinin Long olduğunu varsayıyoruz
    Retweet deleteById(long id);
    
    // Bir tweet'i retweet etme
    Retweet createRetweet(RetweetCreateDTO retweetDTO, User user);
    
    // Bir tweet'in retweet'lerini getirme
    List<Retweet> findRetweetsByTweetId(Long tweetId);
    
    // Bir kullanıcının retweet'lerini getirme
    List<Retweet> findRetweetsByUserId(Long userId);
    
    // Retweet silme - sadece sahibi silebilir
    Retweet deleteRetweetByOwner(Long retweetId, Long userId);
}
