package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Comment;
import com.twitter.mavikus.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    // Bu genelde bir repository olur.
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> findAll() {
        return List.of();
    }

    @Override
    public Comment findById(long id) {
        return null;
    }

    @Override
    public Comment save(Comment instanceOfComment) {
        return null;
    }

    @Override
    public Comment deleteById(long id) {
        return null;
    }
}
