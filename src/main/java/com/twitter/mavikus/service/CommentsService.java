package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Comments;

import java.util.List;
import java.util.Optional;

public interface CommentsService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Comments Entity'lerini getir
    List<Comments> findAll();

    // Read: Belirli bir ID'ye sahip Comments'u getir
    // Optional kullanmak, Comments bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Comments ID'sinin Long olduğunu varsayıyoruz
    Comments findById(long id);

    // Create / Update: Yeni bir Comments kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Comments save(Comments instanceOfComments);

    // Delete: Belirli bir ID'ye sahip Comments öğesini sil
    // Comments ID'sinin Long olduğunu varsayıyoruz
    Comments deleteById(long id);
}
