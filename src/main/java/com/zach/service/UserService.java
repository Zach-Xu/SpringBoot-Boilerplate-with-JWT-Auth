package com.zach.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import org.springframework.stereotype.Service;

public interface UserService {
    ResponseResult login(User user);
}
