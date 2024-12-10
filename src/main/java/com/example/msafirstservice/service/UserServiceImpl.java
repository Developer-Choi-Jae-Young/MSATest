package com.example.msafirstservice.service;

import com.example.msafirstservice.entity.UserEntity;
import com.example.msafirstservice.repository.UserRepository;
import com.example.msafirstservice.vo.RequestUser;
import com.example.msafirstservice.vo.ResponseUser;
import com.example.msafirstservice.vo.ResponseUserItem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(userEntity.getEmail(), userEntity.getPassword(), true, true, true, true, new ArrayList<>());
    }

    public ResponseUser insertUser(RequestUser requestUser) {
        UserEntity user = UserEntity.builder().email(requestUser.getEmail()).name(requestUser.getName()).password(passwordEncoder.encode(requestUser.getPassword())).build();
        userRepository.save(user);
        return ResponseUser.builder().email(user.getEmail()).name(user.getName()).build();
    }

    public ResponseUserItem getUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("exception"));
        return ResponseUserItem.builder().name(user.getName()).email(user.getEmail()).itemDtos(null).build();
    }
}
