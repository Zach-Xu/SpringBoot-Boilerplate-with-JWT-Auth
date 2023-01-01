package com.zach.service.impl;

import com.zach.domain.LoginUser;
import com.zach.domain.User;
import com.zach.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        LoginUser loginUser = user.map(LoginUser::new).orElseThrow(()-> new RuntimeException("Incorrect username or password"));
        return loginUser;
    }
}
