package com.zach.controller;

import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import com.zach.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("hello")
    public ResponseResult<String> hello (){
        return new ResponseResult<>(200,"Hello");
    }

    @PostMapping("login")
    public ResponseResult login(@RequestBody User user){
        return userService.login(user);
    }
}
