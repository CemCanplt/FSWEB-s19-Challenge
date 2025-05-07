package com.twitter.mavikus.dto.tweet;

import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;


/**
 * Tweet entity'si ile ilgili DTO dönüşüm işlemlerini içeren yardımcı sınıf
 */
public final class TweetConvertorDTO {
    
    // Sınıfın instance'ının oluşturulmasını engellemek için private constructor
    private TweetConvertorDTO() {
        throw new UnsupportedOperationException("Bu bir utility sınıfıdır ve instance'lanamaz");
    }
    
    /**
     * Tweet entity'sini TweetResponseDTO'ya dönüştürür
     * @param tweet Dönüştürülecek Tweet nesnesi
     * @return Oluşturulan TweetResponseDTO
     */
    public static TweetResponseDTO toResponseDTO(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        
        // User bilgilerini UserDTO'ya dönüştür
        TweetResponseDTO.UserDTO userDTO = null;
        if (tweet.getUser() != null) {
            userDTO = toUserDTO(tweet.getUser());
        }
        
        return TweetResponseDTO.builder()
                .id(tweet.getId())
                .content(tweet.getContent())
                .createdAt(tweet.getCreatedAt())
                .user(userDTO)
                .likes(tweet.getLikes())
                .comments(tweet.getComments())
                .retweets(tweet.getRetweets())
                .build();
    }
    
    /**
     * Tweet entity'sini TweetUpdateResponseDTO'ya dönüştürür
     * @param tweet Dönüştürülecek Tweet nesnesi
     * @return Oluşturulan TweetUpdateResponseDTO
     */
    public static TweetUpdateResponseDTO toUpdateResponseDTO(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        
        return TweetUpdateResponseDTO.builder()
                .id(tweet.getId())
                .updatedContent(tweet.getContent())
                .updatedAt(tweet.getUpdatedAt())
                .success(true)
                .build();
    }
    
    /**
     * Tweet entity'sini TweetDeleteResponseDTO'ya dönüştürür
     * @param tweet Dönüştürülecek Tweet nesnesi
     * @return Oluşturulan TweetDeleteResponseDTO
     */
    public static TweetDeleteResponseDTO toDeleteResponseDTO(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        
        return TweetDeleteResponseDTO.builder()
                .id(tweet.getId())
                .deletedContent(tweet.getContent())
                .success(true)
                .build();
    }
    
    /**
     * User entity'sini UserDTO'ya dönüştürür
     * @param user Dönüştürülecek User nesnesi
     * @return Oluşturulan UserDTO
     */
    private static TweetResponseDTO.UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return TweetResponseDTO.UserDTO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
