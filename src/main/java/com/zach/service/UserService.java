package com.zach.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    ResponseResult login(User user);

    ResponseResult register(User user);

    ResponseResult logout();
}
