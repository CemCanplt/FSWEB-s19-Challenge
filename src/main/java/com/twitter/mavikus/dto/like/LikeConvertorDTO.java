package com.twitter.mavikus.dto.like;

import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.entity.Tweet;

/**
 * Like entity'si ile ilgili DTO dönüşüm işlemlerini içeren yardımcı sınıf
 */
public final class LikeConvertorDTO {
    
    // Sınıfın instance'ının oluşturulmasını engellemek için private constructor
    private LikeConvertorDTO() {
        throw new UnsupportedOperationException("Bu bir utility sınıfıdır ve instance'lanamaz");
    }
    
    /**
     * Like entity'sini LikeResponseDTO'ya dönüştürür
     * @param like Dönüştürülecek Like nesnesi
     * @return Oluşturulan LikeResponseDTO
     */
    public static LikeResponseDTO toResponseDTO(Like like) {
        if (like == null) {
            return null;
        }
        
        LikeResponseDTO responseDTO = new LikeResponseDTO();
        responseDTO.setId(like.getId());
        
        // Kullanıcı bilgileri
        if (like.getUser() != null) {
            responseDTO.setUserId(like.getUser().getId());
            responseDTO.setUserName(like.getUser().getUsername());
        }
        
        // Tweet bilgileri
        if (like.getTweet() != null) {
            responseDTO.setTweetId(like.getTweet().getId());
            responseDTO.setTweetContent(like.getTweet().getContent());
        }
        
        return responseDTO;
    }
    
    /**
     * Tweet bilgilerinden DislikeResponseDTO oluşturur
     * @param tweet İlgili tweet
     * @return Oluşturulan DislikeResponseDTO
     */
    public static DislikeResponseDTO toDislikeResponseDTO(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        
        return DislikeResponseDTO.builder()
                .tweetId(tweet.getId())
                .tweetContent(tweet.getContent())
                .success(true)
                .message("Tweet beğenisi başarıyla kaldırıldı")
                .build();
    }
}
