package com.twitter.mavikus.service;

import com.twitter.mavikus.dto.CommentCreateDTO;
import com.twitter.mavikus.dto.CommentUpdateDTO;
import com.twitter.mavikus.entity.Comment;
import com.twitter.mavikus.entity.Tweet;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.CommentRepository;
import com.twitter.mavikus.repository.TweetRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.Instant;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen yorum bulunamadı: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Comment save(Comment instanceOfComment) {
        return commentRepository.save(instanceOfComment);
    }

    @Override
    public Comment deleteById(long id) {
        Comment comment = findById(id);
        commentRepository.deleteById(id);
        return comment;
    }
    
    @Override
    @Transactional
    public Comment createComment(CommentCreateDTO commentDTO, User user) {
        // Tweet'i ID'ye göre bul
        Tweet tweet = tweetRepository.findById(commentDTO.getTweetId())
                .orElseThrow(() -> new MaviKusErrorException("Bu id ile eşleşen tweet bulunamadı: " + commentDTO.getTweetId(), 
                        HttpStatus.NOT_FOUND));
        
        // Yorum metnini kontrol et
        if (commentDTO.getCommentText() == null || commentDTO.getCommentText().trim().isEmpty()) {
            throw new MaviKusErrorException("Yorum içeriği boş olamaz", HttpStatus.BAD_REQUEST);
        }
        
        // Yeni yorum nesnesi oluştur
        Comment comment = new Comment();
        comment.setCommentText(commentDTO.getCommentText());
        comment.setUser(user);
        comment.setTweet(tweet);
        
        // Yorumu kaydet ve döndür
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, CommentUpdateDTO commentUpdateDTO, Long currentUserId) {
        // Yorumu ID'ye göre bul
        Comment comment = findById(commentId);
        
        // Yorumu güncelleyen kullanıcının yorum sahibi olup olmadığını kontrol et
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new MaviKusErrorException("Bu yorumu güncelleme yetkiniz yok! Yorum sadece sahibi tarafından güncellenebilir.", 
                                          HttpStatus.FORBIDDEN);
        }
        
        // Yorum metnini kontrol et
        if (commentUpdateDTO.getCommentText() == null || commentUpdateDTO.getCommentText().trim().isEmpty()) {
            throw new MaviKusErrorException("Yorum içeriği boş olamaz", HttpStatus.BAD_REQUEST);
        }
        
        // Yorumu güncelle
        comment.setCommentText(commentUpdateDTO.getCommentText());
        comment.setUpdateAt(Instant.now()); // Güncelleme zamanını ayarla
        
        // Güncellenmiş yorumu kaydet ve döndür
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment deleteCommentByOwner(Long commentId, Long currentUserId) {
        // Yorumu ID'ye göre bul
        Comment comment = findById(commentId);
        
        // Kullanıcı yorum sahibi mi veya tweet sahibi mi kontrol et
        boolean isCommentOwner = comment.getUser().getId().equals(currentUserId);
        boolean isTweetOwner = comment.getTweet().getUser().getId().equals(currentUserId);
        
        if (!isCommentOwner && !isTweetOwner) {
            throw new MaviKusErrorException("Bu yorumu silme yetkiniz yok! Yorum sadece yorumun sahibi veya tweet'in sahibi tarafından silinebilir.", 
                                          HttpStatus.FORBIDDEN);
        }
        
        // Yorum silme işleminden önce referansı tut
        Comment deletedComment = new Comment();
        deletedComment.setId(comment.getId());
        deletedComment.setCommentText(comment.getCommentText());
        deletedComment.setUser(comment.getUser());
        deletedComment.setTweet(comment.getTweet());
        deletedComment.setCreatedAt(comment.getCreatedAt());
        deletedComment.setUpdateAt(comment.getUpdateAt());
        
        // Yorumu sil ve referansı döndür
        commentRepository.deleteById(commentId);
        return deletedComment;
    }
}
