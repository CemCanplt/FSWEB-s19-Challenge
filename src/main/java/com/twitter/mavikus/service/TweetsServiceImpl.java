package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Tweets;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.TweetsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TweetsServiceImpl implements TweetsService {

    // Bu genelde bir repository olur.
    private final TweetsRepository tweetsRepository;

    @Override
    @Transactional
    public List<Tweets> findAll() {
        return tweetsRepository.findAll();
    }

    @Override
    @Transactional
    public Tweets findById(long id) {
        return tweetsRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Tweets save(Tweets instanceOfTweets) {
        return tweetsRepository.save(instanceOfTweets);
    }

    @Override
    @Transactional
    public Tweets deleteById(long id) {
        Tweets tweet = tweetsRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));
        tweetsRepository.deleteById(id);
        return tweet;
    }

    @Override
    @Transactional
    public Tweets update(long id, String text) {
        Tweets tweet = tweetsRepository.findById(id).orElseThrow(()-> new MaviKusErrorException("Bu id ile eşleşen Tweet bulunamadı: "+ id, HttpStatus.NOT_FOUND));

        tweet.setContent(text);

        return tweetsRepository.save(tweet);
    }
}
