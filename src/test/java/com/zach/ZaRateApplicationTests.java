package com.zach;

import com.zach.domain.User;
import com.zach.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZaRateApplicationTests {
    @Autowired
    private UserRepository userRepository;

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

}
