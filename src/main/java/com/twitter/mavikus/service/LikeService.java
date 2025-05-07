package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.like.DislikeResponseDTO;
import com.twitter.mavikus.dto.like.LikeCreateDTO;
import com.twitter.mavikus.dto.like.LikeResponseDTO;
import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.User;

import java.util.List;

public interface LikeService {
    // CRUD Operasyonları
    List<Like> findAll();
    Like findById(long id);
    Like save(Like instanceOfLike);
    Like deleteById(long id);
    
    // Beğeni ekleme metodu
    Like addLike(LikeCreateDTO likeDTO, User currentUser);
    
    // Beğeni ekleme metodu (DTO dönüşü)
    LikeResponseDTO addLikeAndReturnDTO(LikeCreateDTO likeDTO, User currentUser);
    
    // Tweet'e ait beğenileri getirme metodu
    List<Like> findLikesByTweetId(long tweetId);
    
    // Kullanıcıya ait beğenileri getirme metodu
    List<Like> findLikesByUserId(long userId);
    
    // Beğeni silme metodu (DTO dönüşü)
    DislikeResponseDTO removeLikeAndReturnDTO(long tweetId, long userId);
}
