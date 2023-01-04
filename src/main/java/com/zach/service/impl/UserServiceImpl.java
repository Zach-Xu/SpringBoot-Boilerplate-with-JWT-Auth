package com.zach.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zach.domain.LoginUser;
import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import com.zach.dto.UserDTO;
import com.zach.service.UserService;
import com.zach.utils.JWTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zach.utils.RedisConst.LOGIN_USER_KEY;

@Service
public class UserServiceImpl implements UserService {


    final private AuthenticationManager authenticationManager;

    @Resource
    RedisTemplate redisTemplate;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseResult login(User user) {
        // Authenticate user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("Login fail");
        }

        // Authenticate successful
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        // Generate JWT
        User userDB = loginUser.getUser();
        String userId = userDB.getId().toString();
        String token = JWTUtils.createJWT(userId);


        // DTO to exclude unnecessary fields
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDB, userDTO);

        // store in redis
        redisTemplate.opsForValue().set(LOGIN_USER_KEY + token, userDTO);

        Map<String,Object> userMap = new HashMap<>();
        userMap.put("user", userDTO);
        userMap.put("token", token);

        // return token along with user info
        return new ResponseResult(200, "Login successful", userMap);
    }
}
