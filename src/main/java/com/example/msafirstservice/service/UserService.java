package com.example.msafirstservice.service;

import com.example.msafirstservice.vo.RequestUser;
import com.example.msafirstservice.vo.ResponseUser;
import com.example.msafirstservice.vo.ResponseUserItem;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public ResponseUser insertUser(RequestUser requestUser);

    public ResponseUserItem getUser(Long userId);
}
