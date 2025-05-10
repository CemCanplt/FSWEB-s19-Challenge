package com.twitter.mavikus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.mavikus.dto.like.DislikeResponseDTO;
import com.twitter.mavikus.dto.like.LikeCreateDTO;
import com.twitter.mavikus.dto.like.LikeResponseDTO;
import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Integration Test ➜ Entegrasyon Testi
// Parçalar birlikte mantıklı çalışıyor mu?

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;   
    // Not: @MockBean Spring Boot 3.4.0+ sürümlerinde kullanımdan kaldırılacak.
    // İlerleyen sürümlerde @MockitoBean kullanılabilir
    @MockitoBean
    private LikeService likeService;
    
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Beğeni başarıyla eklendiğinde 201 Created dönmeli")
    @WithMockUser(username = "testUser") // Security'i aşması için böyle
    void addLike_ShouldReturnCreated() throws Exception {
        // Given - Hazırlık
        LikeCreateDTO likeCreateDTO = new LikeCreateDTO(1L);
        
        LikeResponseDTO responseDTO = new LikeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUserId(1L);
        responseDTO.setUserName("testUser");
        responseDTO.setTweetId(1L);
        responseDTO.setTweetContent("Test tweet");
          // Daha esnek bir mock ayarı - Spring Security'nin ortam değişkenlerine uyum sağlar
        when(likeService.addLikeAndReturnDTO(any(LikeCreateDTO.class), any()))
            .thenReturn(responseDTO);
        
        // When & Then - Eylem & Doğrulama
        mockMvc.perform(post("/like").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(likeCreateDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("testUser"))
                .andExpect(jsonPath("$.tweetId").value(1))
                .andExpect(jsonPath("$.tweetContent").value("Test tweet"))
                .andDo(print());
    }

    @Test
    @DisplayName("Tweet'e ait beğenileri başarıyla getirmeli")
    @WithMockUser(username = "testUser") // Security'i aşması için böyle
    void getLikesByTweet_ShouldReturnLikes() throws Exception {
        // Given - Hazırlık
        Long tweetId = 1L;
        
        // Test için mock nesneleri oluştur
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        tweet.setContent("Test tweet");
        
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        
        Like like1 = new Like();
        like1.setId(1L);
        like1.setTweet(tweet);
        like1.setUser(user);
        
        Like like2 = new Like();
        like2.setId(2L);
        like2.setTweet(tweet);
        like2.setUser(user);
        
        List<Like> likeList = Arrays.asList(like1, like2);
        
        when(likeService.findLikesByTweetId(tweetId)).thenReturn(likeList);
        
        // When & Then - Eylem & Doğrulama
        mockMvc.perform(get("/like/tweet/{tweetId}", tweetId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("Kullanıcıya ait beğenileri başarıyla getirmeli")
    @WithMockUser(username = "testUser") // Security'i aşması için böyle
    void getLikesByUser_ShouldReturnLikes() throws Exception {
        // Given - Hazırlık
        Long userId = 1L;
        
        // Test için mock nesneleri oluştur
        Tweet tweet1 = new Tweet();
        tweet1.setId(1L);
        tweet1.setContent("Test tweet 1");
        
        Tweet tweet2 = new Tweet();
        tweet2.setId(2L);
        tweet2.setContent("Test tweet 2");
        
        User user = new User();
        user.setId(userId);
        user.setUserName("testUser");
        
        Like like1 = new Like();
        like1.setId(1L);
        like1.setTweet(tweet1);
        like1.setUser(user);
        
        Like like2 = new Like();
        like2.setId(2L);
        like2.setTweet(tweet2);
        like2.setUser(user);
        
        List<Like> likeList = Arrays.asList(like1, like2);
        
        when(likeService.findLikesByUserId(userId)).thenReturn(likeList);
        
        // When & Then - Eylem & Doğrulama
        mockMvc.perform(get("/like/user/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andDo(print());
    }    @Test
    @DisplayName("Beğeni başarıyla kaldırıldığında 200 OK dönmeli")
    @WithMockUser(username = "testUser") // Security'i aşması için böyle
    void dislike_ShouldReturnOk() throws Exception {
        // Given - Hazırlık
        LikeCreateDTO likeCreateDTO = new LikeCreateDTO(1L);
        
        DislikeResponseDTO responseDTO = new DislikeResponseDTO();
        responseDTO.setTweetId(1L);
        responseDTO.setTweetContent("Test tweet");
        responseDTO.setSuccess(true);
        responseDTO.setMessage("Beğeni başarıyla kaldırıldı");
        
        // Controller'a gönderilecek UserId değerinin dönüş değerini bypass ediyoruz
        // Artık herhangi bir ID değeri için aynı yanıt dönecek
        when(likeService.removeLikeAndReturnDTO(anyLong(), anyLong()))
            .thenReturn(responseDTO);
        
        // When & Then - Eylem & Doğrulama
        mockMvc.perform(post("/dislike").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(likeCreateDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tweetId").value(1))
                .andExpect(jsonPath("$.tweetContent").value("Test tweet"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Beğeni başarıyla kaldırıldı"))
                .andDo(print());
    }
    
    public String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}