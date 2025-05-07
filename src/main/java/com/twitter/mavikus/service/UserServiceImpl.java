package com.twitter.mavikus.service;

import com.twitter.mavikus.entity.User;
import com.twitter.mavikus.exceptions.MaviKusErrorException;
import com.twitter.mavikus.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

// Bu genelde bir repository olur.
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new MaviKusErrorException("Bu ID ile eşleşen kullanıcı bulunamadı: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public User save(User instanceOfUser) {
        return userRepository.save(instanceOfUser);
    }

    @Override
    @Transactional
    public User deleteById(long id) {
        User user = findById(id);
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUserName(username).orElseThrow(()-> {
            System.out.println("Kullanıcı bilgileri hatalı: " + username);
            throw new UsernameNotFoundException("Kullanıcı bilgileri hatalı: " + username);
        });
    }
}
