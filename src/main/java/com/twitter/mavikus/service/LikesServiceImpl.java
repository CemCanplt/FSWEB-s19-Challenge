package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Likes;
import com.twitter.mavikus.repository.LikesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {

    // Bu genelde bir repository olur.
    private final LikesRepository likesRepository;

    @Override
    public List<Likes> findAll() {
        return List.of();
    }

    @Override
    public Likes findById(long id) {
        return null;
    }

    @Override
    public Likes save(Likes instanceOfLikes) {
        return null;
    }

    @Override
    public Likes deleteById(long id) {
        return null;
    }
}
