package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.like.DislikeResponseDTO;
import com.twitter.mavikus.dto.like.LikeCreateDTO;
import com.twitter.mavikus.dto.like.LikeResponseDTO;
import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.LikeRepository;
import com.twitter.mavikus.repository.TweetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private TweetRepository tweetRepository;

    @BeforeEach
    void setUp() {
        likeService = new LikeServiceImpl(likeRepository, tweetRepository);
    }

    @DisplayName("Tüm beğenileri başarıyla getirmeli")
    @Test
    void findAll_ShouldReturnAllLikes() {
        // Given
        List<Like> likes = new ArrayList<>();
        when(likeRepository.findAll()).thenReturn(likes);
        
        // When
        List<Like> result = likeService.findAll();
        
        // Then
        verify(likeRepository).findAll();
        assertThat(result).isEqualTo(likes);
    }

    @DisplayName("Geçerli ID ile beğeni bulabilmeli")
    @Test
    void findById_WithValidId_ShouldReturnLike() {
        // Given
        Long likeId = 1L;
        Like like = new Like();
        like.setId(likeId);
        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));
        
        // When
        Like result = likeService.findById(likeId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(likeId);
        verify(likeRepository).findById(likeId);
    }
    
    @DisplayName("Geçersiz ID ile beğeni bulamadığında hata fırlatmalı")
    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        Long likeId = 999L;
        when(likeRepository.findById(likeId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> likeService.findById(likeId))
            .isInstanceOf(MaviKusErrorException.class)
            .hasMessageContaining("Bu id ile eşleşen beğeni bulunamadı")
            .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.NOT_FOUND);
    }

    @DisplayName("Beğeni kaydedebilmeli")
    @Test
    void save_ShouldSaveLike() {
        // Given
        Like like = new Like();
        when(likeRepository.save(like)).thenReturn(like);
        
        // When
        Like savedLike = likeService.save(like);
        
        // Then
        assertThat(savedLike).isEqualTo(like);
        verify(likeRepository).save(like);
    }
    
    @DisplayName("ID ile beğeni silebilmeli")
    @Test
    void deleteById_WithValidId_ShouldDeleteLikeAndReturnIt() {
        // Given
        Long likeId = 1L;
        Like like = new Like();
        like.setId(likeId);
        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));
        
        // When
        Like deletedLike = likeService.deleteById(likeId);
        
        // Then
        assertThat(deletedLike).isEqualTo(like);
        verify(likeRepository).findById(likeId);
        verify(likeRepository).deleteById(likeId);
    }

    @DisplayName("Beğeni başarıyla ekleyebilmeli")
    @Test
    void addLike_ShouldAddLike() {
        // Given
        Long tweetId = 1L;
        LikeCreateDTO likeDTO = new LikeCreateDTO();
        likeDTO.setTweetId(tweetId);
        
        User user = new User();
        user.setId(1L);
        
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        
        when(tweetRepository.findById(tweetId)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByTweetIdAndUserId(tweetId, user.getId())).thenReturn(Optional.empty());
        
        Like savedLike = new Like();
        savedLike.setUser(user);
        savedLike.setTweet(tweet);
        when(likeRepository.save(any(Like.class))).thenReturn(savedLike);
        
        // When
        Like result = likeService.addLike(likeDTO, user);
        
        // Then
        assertThat(result).isEqualTo(savedLike);
        verify(tweetRepository).findById(tweetId);
        verify(likeRepository).findByTweetIdAndUserId(tweetId, user.getId());
        
        ArgumentCaptor<Like> likeCaptor = ArgumentCaptor.forClass(Like.class);
        verify(likeRepository).save(likeCaptor.capture());
        
        Like capturedLike = likeCaptor.getValue();
        assertThat(capturedLike.getTweet()).isEqualTo(tweet);
        assertThat(capturedLike.getUser()).isEqualTo(user);
    }

    @DisplayName("Beğeni ekleyip DTO döndürebilmeli")
    @Test
    void addLikeAndReturnDTO_ShouldAddLikeAndReturnDTO() {
        // Given
        Long tweetId = 1L;
        LikeCreateDTO likeDTO = new LikeCreateDTO();
        likeDTO.setTweetId(tweetId);
        
        User user = new User();
        user.setId(1L);
        
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        
        Like like = new Like();
        like.setId(1L);
        like.setUser(user);
        like.setTweet(tweet);
        
        when(tweetRepository.findById(tweetId)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByTweetIdAndUserId(tweetId, user.getId())).thenReturn(Optional.empty());
        when(likeRepository.save(any(Like.class))).thenReturn(like);
        
        // When
        LikeResponseDTO responseDTO = likeService.addLikeAndReturnDTO(likeDTO, user);
        
        // Then
        assertThat(responseDTO).isNotNull();
        verify(tweetRepository).findById(tweetId);
    }

    @DisplayName("Tweet ID ile beğenileri bulabilmeli")
    @Test
    void findLikesByTweetId_ShouldFindLikesByTweetId() {
        // Given
        Long tweetId = 1L;
        List<Like> likes = new ArrayList<>();
        
        when(tweetRepository.existsById(tweetId)).thenReturn(true);
        when(likeRepository.findByTweetId(tweetId)).thenReturn(likes);
        
        // When
        List<Like> result = likeService.findLikesByTweetId(tweetId);
        
        // Then
        assertThat(result).isEqualTo(likes);
        verify(tweetRepository).existsById(tweetId);
        verify(likeRepository).findByTweetId(tweetId);
    }

    @DisplayName("Kullanıcı ID ile beğenileri bulabilmeli")
    @Test
    void findLikesByUserId_ShouldFindLikesByUserId() {
        // Given
        Long userId = 1L;
        List<Like> likes = new ArrayList<>();
        
        when(likeRepository.findByUserId(userId)).thenReturn(likes);
        
        // When
        List<Like> result = likeService.findLikesByUserId(userId);
        
        // Then
        assertThat(result).isEqualTo(likes);
        verify(likeRepository).findByUserId(userId);
    }

    @DisplayName("Beğeniyi kaldırıp DTO döndürebilmeli")
    @Test
    void removeLikeAndReturnDTO_ShouldRemoveLikeAndReturnDTO() {
        // Given
        Long tweetId = 1L;
        Long userId = 1L;
        
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        
        Like like = new Like();
        like.setId(1L);
        like.setTweet(tweet);
        
        when(likeRepository.findByTweetIdAndUserId(tweetId, userId)).thenReturn(Optional.of(like));
        
        // When
        DislikeResponseDTO responseDTO = likeService.removeLikeAndReturnDTO(tweetId, userId);
        
        // Then
        assertThat(responseDTO).isNotNull();
        verify(likeRepository).findByTweetIdAndUserId(tweetId, userId);
        verify(likeRepository).deleteById(like.getId());
    }
}