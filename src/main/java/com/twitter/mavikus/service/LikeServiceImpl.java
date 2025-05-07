package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Like;
import com.twitter.mavikus.repository.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

    // Bu genelde bir repository olur.
    private final LikeRepository likeRepository;

    @Override
    public List<Like> findAll() {
        return List.of();
    }

    @Override
    public Like findById(long id) {
        return null;
    }

    @Override
    public Like save(Like instanceOfLike) {
        return null;
    }

    @Override
    public Like deleteById(long id) {
        return null;
    }
}
