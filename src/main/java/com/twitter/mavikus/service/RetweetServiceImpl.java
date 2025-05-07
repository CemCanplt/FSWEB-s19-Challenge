package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Retweet;
import com.twitter.mavikus.repository.RetweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RetweetServiceImpl implements RetweetService {

    // Bu genelde bir repository olur.
    private final RetweetRepository retweetRepository;

    @Override
    public List<Retweet> findAll() {
        return List.of();
    }

    @Override
    public Retweet findById(long id) {
        return null;
    }

    @Override
    public Retweet save(Retweet instanceOfRetweet) {
        return null;
    }

    @Override
    public Retweet deleteById(long id) {
        return null;
    }
}
