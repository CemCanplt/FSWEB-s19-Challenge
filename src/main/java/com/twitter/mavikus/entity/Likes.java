package com.twitter.mavikus.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "likes", schema = "public")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;
    // ID değişkeni genelde Long olur. Eğer başka bir tür kullanıyorsan, bunu kendine göre değiştir.

    @ManyToOne
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweets tweets;


    // CascadeType.ALL, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    // @ManyToMany için :@join:t yazabilirsin.
    // Default olarak LAZY gelir.
    @ManyToOne // Birçok Tweet -> Bir Kullanıcı
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
