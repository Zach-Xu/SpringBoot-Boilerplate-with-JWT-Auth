package com.zach.service.impl;

import com.zach.domain.LoginUser;
import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import com.zach.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {


    final private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseResult login(User user) {
        // Authenticate user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)){
            throw new RuntimeException("Login fail");
        }

        // Authenticate successful
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        return new ResponseResult(200,"Login successful", loginUser);
    }
}
