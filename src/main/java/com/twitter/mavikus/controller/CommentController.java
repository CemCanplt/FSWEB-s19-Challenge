package com.twitter.mavikus.controller;

import com.twitter.mavikus.dto.CommentCreateDTO;
import com.twitter.mavikus.dto.CommentUpdateDTO;
import com.twitter.mavikus.entity.Comment;
import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@AllArgsConstructor
@RestController
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody @Valid CommentCreateDTO commentDTO, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Yorumu oluştur ve kaydet
        Comment createdComment = commentService.createComment(commentDTO, currentUser);
        
        // HTTP 201 Created statüsü ile birlikte oluşturulan yorumu döndür
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, 
                                               @RequestBody @Valid CommentUpdateDTO commentUpdateDTO, 
                                               Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Service katmanına iş mantığını devret, kullanıcı ID'sini de geçiriyoruz
        Comment updatedComment = commentService.updateComment(id, commentUpdateDTO, currentUser.getId());
        
        // Güncellenmiş yorumu HTTP 200 OK statüsü ile birlikte döndür
        return ResponseEntity.ok(updatedComment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long id, Authentication authentication) {
        // Giriş yapmış kullanıcıyı al
        User currentUser = (User) authentication.getPrincipal();
        
        // Service katmanına iş mantığını devret, kullanıcı ID'sini de geçiriyoruz
        Comment deletedComment = commentService.deleteCommentByOwner(id, currentUser.getId());
        
        // Silinen yorumu HTTP 200 OK statüsü ile birlikte döndür
        return ResponseEntity.ok(deletedComment);
    }
}
