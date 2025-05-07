package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Like;

import java.util.List;

public interface LikeService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Likes Entity'lerini getir
    List<Like> findAll();

    // Read: Belirli bir ID'ye sahip Likes'u getir
    // Optional kullanmak, Likes bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Likes ID'sinin Long olduğunu varsayıyoruz
    Like findById(long id);

    // Create / Update: Yeni bir Likes kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Like save(Like instanceOfLike);

    // Delete: Belirli bir ID'ye sahip Likes öğesini sil
    // Likes ID'sinin Long olduğunu varsayıyoruz
    Like deleteById(long id);
}
