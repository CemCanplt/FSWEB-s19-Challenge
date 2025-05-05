package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Comments;
import com.twitter.mavikus.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    // Bu genelde bir repository olur.
    private final CommentsRepository commentsRepository;

    @Override
    public List<Comments> findAll() {
        return List.of();
    }

    @Override
    public Comments findById(long id) {
        return null;
    }

    @Override
    public Comments save(Comments instanceOfComments) {
        return null;
    }

    @Override
    public Comments deleteById(long id) {
        return null;
    }
}
