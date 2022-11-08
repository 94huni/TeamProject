package com.springboot.Teamproject.service;

import com.springboot.Teamproject.DataNotFoundException;
import com.springboot.Teamproject.entity.User;
import com.springboot.Teamproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void create(String id, String password, String nickname){

        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);

        this.userRepository.save(user);
    }

    public User getUser(String id){
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        else
            throw new DataNotFoundException("유저를 찾을 수 없습니다");
    }

    public void delete(User user){

        this.userRepository.delete(user);
    }
}
