package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Users;
import com.twitter.mavikus.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {

    // Bu genelde bir repository olur.
    private final UsersRepository usersRepository;

    @Override
    public List<Users> findAll() {
        return List.of();
    }

    @Override
    public Users findById(long id) {
        return null;
    }

    @Override
    public Users save(Users instanceOfUsers) {
        return null;
    }

    @Override
    public Users deleteById(long id) {
        return null;
    }
}
