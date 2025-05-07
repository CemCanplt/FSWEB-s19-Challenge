package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Comment;

import java.util.List;

public interface CommentService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Comments Entity'lerini getir
    List<Comment> findAll();

    // Read: Belirli bir ID'ye sahip Comments'u getir
    // Optional kullanmak, Comments bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Comments ID'sinin Long olduğunu varsayıyoruz
    Comment findById(long id);

    // Create / Update: Yeni bir Comments kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Comment save(Comment instanceOfComment);

    // Delete: Belirli bir ID'ye sahip Comments öğesini sil
    // Comments ID'sinin Long olduğunu varsayıyoruz
    Comment deleteById(long id);
}
