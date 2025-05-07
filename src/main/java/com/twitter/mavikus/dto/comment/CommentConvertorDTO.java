package com.twitter.mavikus.dto.comment;

import com.twitter.mavikus.entity.Comment;

/**
 * Comment entity'si ile ilgili DTO dönüşüm işlemlerini içeren yardımcı sınıf
 */
public final class CommentConvertorDTO {
    
    // Sınıfın instance'ının oluşturulmasını engellemek için private constructor
    private CommentConvertorDTO() {
        throw new UnsupportedOperationException("Bu bir utility sınıfıdır ve instance'lanamaz");
    }
    
    /**
     * Comment entity'sini CommentResponseDTO'ya dönüştürür
     * @param comment Dönüştürülecek Comment nesnesi
     * @return Oluşturulan CommentResponseDTO
     */
    public static CommentResponseDTO toResponseDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setCommentText(comment.getCommentText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        
        // Kullanıcı bilgileri (sadece gerekli olanlar)
        if (comment.getUser() != null) {
            dto.setUserId(comment.getUser().getId());
            dto.setUserName(comment.getUser().getUsername());
        }
        
        // Tweet bilgileri (sadece gerekli olanlar)
        if (comment.getTweet() != null) {
            dto.setTweetId(comment.getTweet().getId());
        }
        
        return dto;
    }
}
