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

    //회원가입 시 적었던 내용으로 유저 정보 등록
    public void create(String id, String password, String nickname){

        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);

        this.userRepository.save(user);
    }

    //아이디 정보를 기반으로 유저 정보를 가져옴
    public User getUser(String id){
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        else
            throw new DataNotFoundException("유저를 찾을 수 없습니다");
    }

    //유저 탈퇴 시 유저 정보를 삭제
    public void delete(User user){

        this.userRepository.delete(user);
    }
}
