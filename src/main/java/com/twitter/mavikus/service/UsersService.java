package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Users;

import java.util.List;

public interface UsersService {
    // Buraya servis fonksiyonlarını yazabilirsin.
    // Repository'de yazdıklarını buraya getirmeni öneririm.

    // CRUD Operasyonları

    // Read: Tüm Users Entity'lerini getir
    List<Users> findAll();

    // Read: Belirli bir ID'ye sahip Users'u getir
    // Optional kullanılabilir, Users bulunamazsa null yerine boş bir Optional dönmemizi sağlar.
    // Users ID'sinin Long olduğunu varsayıyoruz
    Users findById(long id);

    // Create / Update: Yeni bir Users kaydet veya var olanı güncelle
    // Spring Data JPA'nın save metodu ID varsa günceller, yoksa yeni kaydeder.
    Users save(Users instanceOfUsers);

    // Delete: Belirli bir ID'ye sahip Users öğesini sil
    // Users ID'sinin Long olduğunu varsayıyoruz
    Users deleteById(long id);
}
