package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Retweets;
import com.twitter.mavikus.repository.RetweetsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RetweetsServiceImpl implements RetweetsService {

    // Bu genelde bir repository olur.
    private final RetweetsRepository retweetsRepository;

    @Override
    public List<Retweets> findAll() {
        return List.of();
    }

    @Override
    public Retweets findById(long id) {
        return null;
    }

    @Override
    public Retweets save(Retweets instanceOfRetweets) {
        return null;
    }

    @Override
    public Retweets deleteById(long id) {
        return null;
    }
}
