package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Tweets;
import com.twitter.mavikus.repository.TweetsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TweetsServiceImpl implements TweetsService {

    // Bu genelde bir repository olur.
    private final TweetsRepository tweetsRepository;

    @Override
    public List<Tweets> findAll() {
        return List.of();
    }

    @Override
    public Tweets findById(long id) {
        return null;
    }

    @Override
    public Tweets save(Tweets instanceOfTweets) {
        return null;
    }

    @Override
    public Tweets deleteById(long id) {
        return null;
    }
}
