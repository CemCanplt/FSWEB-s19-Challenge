package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.Users;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {

// Bu genelde bir repository olur.
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional
    public Users findById(long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu ID ile eşleşen kullanıcı bulunamadı: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Users save(Users instanceOfUsers) {
        return usersRepository.save(instanceOfUsers);
    }

    @Override
    @Transactional
    public Users deleteById(long id) {
        Users user = findById(id);
        usersRepository.deleteById(id);
        return user;
    }
}
