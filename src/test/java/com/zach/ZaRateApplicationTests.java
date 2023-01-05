package com.zach;

import com.zach.domain.User;
import com.zach.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ZaRateApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void createUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("$2a$12$cxbGLbSfU2vTIVOYWErX0eIalGuhGm4HEGxJXxZYfAT4O1pJUDH6C");
        user.setPhone("4161234567");
        userRepository.save(user);
    }

    @Test
    void encodePassword(){
        String password ="randomTestPassword";
        String encode = passwordEncoder.encode(password);
        System.out.println("encode = " + encode);
        boolean isMatched = passwordEncoder.matches(password, encode);
        System.out.println("isMatched = " + isMatched);

        String password2 = "$2a$12$cxbGLbSfU2vTIVOYWErX0eIalGuhGm4HEGxJXxZYfAT4O1pJUDH6C";
        boolean isMatched2 = passwordEncoder.matches("123456", password2);
        System.out.println("isMatched2 = " + isMatched);

    }


}
