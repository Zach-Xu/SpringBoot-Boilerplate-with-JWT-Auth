package com.zach.service;

import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import org.springframework.stereotype.Service;

public interface UserService {
    ResponseResult login(User user);
}
