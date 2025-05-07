package com.twitter.mavikus.dto.retweet;

import com.twitter.mavikus.entity.Retweet;

/**
 * Retweet entity'si ile ilgili DTO dönüşüm işlemlerini içeren yardımcı sınıf
 */
public final class RetweetConvertorDTO {
    
    // Sınıfın instance'ının oluşturulmasını engellemek için private constructor
    private RetweetConvertorDTO() {
        throw new UnsupportedOperationException("Bu bir utility sınıfıdır ve instance'lanamaz");
    }
    
    /**
     * Retweet entity'sini RetweetResponseDTO'ya dönüştürür
     * @param retweet Dönüştürülecek Retweet nesnesi
     * @return Oluşturulan RetweetResponseDTO
     */
    public static RetweetResponseDTO toResponseDTO(Retweet retweet) {
        if (retweet == null) {
            return null;
        }
        
        RetweetResponseDTO responseDTO = new RetweetResponseDTO();
        responseDTO.setId(retweet.getId());
        
        // Kullanıcı bilgileri
        if (retweet.getUser() != null) {
            responseDTO.setUserId(retweet.getUser().getId());
            responseDTO.setUserName(retweet.getUser().getUsername());
        }
        
        // Tweet bilgileri - originalTweet değişkeni kullanılmalı
        if (retweet.getOriginalTweet() != null) {
            responseDTO.setTweetId(retweet.getOriginalTweet().getId());
            responseDTO.setTweetContent(retweet.getOriginalTweet().getContent());
        }
        
        return responseDTO;
    }
}
