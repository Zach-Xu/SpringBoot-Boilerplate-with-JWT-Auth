package com.zach.controller;

import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import com.zach.service.UserService;
import com.zach.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("hello")
    public ResponseResult hello (){
        return new ResponseResult<>(200,"Hello");
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return userService.login(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String messages = WebUtils.getErrorMessages(bindingResult);
            throw new IllegalArgumentException(messages);
        }
        return  userService.register(user);
    }

    @GetMapping("/logout")
    public ResponseResult logout(){
        return userService.logout();
    }

}
