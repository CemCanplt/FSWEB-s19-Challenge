package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Likes;

import java.util.List;
import java.util.Optional;

public interface LikesService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Likes Entity'lerini getir
    List<Likes> findAll();

    // Read: Belirli bir ID'ye sahip Likes'u getir
    // Optional kullanmak, Likes bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Likes ID'sinin Long olduğunu varsayıyoruz
    Likes findById(long id);

    // Create / Update: Yeni bir Likes kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Likes save(Likes instanceOfLikes);

    // Delete: Belirli bir ID'ye sahip Likes öğesini sil
    // Likes ID'sinin Long olduğunu varsayıyoruz
    Likes deleteById(long id);
}
