package com.twitter.mavikus.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "public")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    // ID değişkeni genelde Long olur. Eğer başka bir tür kullanıyorsan, bunu kendine göre değiştir.

    @Column(name = "user_name")
    @NotBlank(message = "Kullanıcı adı boş olamaz!")
    @Size(min = 1, max = 100, message = "Kullanıcı adı 1-100 arası karakter olmalıdır.")
    private String userName;

    @Column(name = "password")
    @NotBlank(message = "Şifre boş olamaz!")
    @Size(min = 1, max = 100, message = "Şifre 1-100 arası karakter olmalıdır.")
    private String password;

    @Column(name = "email")
    @NotBlank(message = "E-mail adresi boş olamaz!")
    @Size(min = 1, max = 255, message = "E-mail 1-255 arası karakter olmalıdır.")
    private String email;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt; // PostgreSQL TIMESTAMP WITH TIME ZONE sütunu için

    // CascadeType.ALL, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    // @ManyToMany için :@join:t yazabilirsin.
    // Default olarak LAZY gelir.
    // mappedBy="karşıdaki_ManyToOne_alanının_adı"
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    List<Tweets> tweets = new ArrayList<>();

    // İsteğe bağlı olarak, ilişkiyi her iki taraftan da kurmaya yardımcı metodlar (Bu en iyi pratiktir)
//    public void addTweet(Tweets tweet) {
//        if (tweet != null && !this.tweets.contains(tweet)) {
//            this.tweets.add(tweet);
//            tweet.setUser(this); // Tweet'in de user'ını ayarla
//        }
//    }
//
//    public void removeTweet(Tweets tweet) {
//        if (tweet != null && this.tweets.contains(tweet)) {
//            this.tweets.remove(tweet);
//            tweet.setUser(null); // Tweet'in user'ını kaldır
//        }
//    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    List<Likes> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    List<Comments> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    List<Retweets> retweets = new ArrayList<>();

}
