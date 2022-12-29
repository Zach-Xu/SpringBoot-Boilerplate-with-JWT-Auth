package com.zach.controller;

import com.zach.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("hello")
    public ResponseResult<String> hello (){
        return new ResponseResult<>(200,"Hello");
    }
}
