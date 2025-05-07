package com.twitter.mavikus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tweet", schema = "public")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    private Long id;
    // ID değişkeni genelde Long olur. Eğer başka bir tür kullanıyorsan, bunu kendine göre değiştir.

    @ManyToOne // Birçok Tweet -> Bir Kullanıcı
    @JsonBackReference // Bu, döngüsel referansı önlemek için kullanılır.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content")
    @NotBlank(message = "İçerik boş bırakılamaz.")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at") //, updatable = false) // updatable=false ekledim, bu alan güncellenmeyecek
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at") //, insertable = false) // insertable=false ekledim, bu alan ilk oluşturma sırasında veritabanı default değeri kullanılacak
    private Instant updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tweet")
    @JsonManagedReference 
    List<Like> likes = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tweet")
    @JsonManagedReference 
    List<Comment> comments = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "originalTweet")
    @JsonManagedReference 
    List<Retweet> retweets = new ArrayList<>();
}
