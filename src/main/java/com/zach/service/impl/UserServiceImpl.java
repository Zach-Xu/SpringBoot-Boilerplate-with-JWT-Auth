package com.zach.service.impl;

import com.zach.domain.LoginUser;
import com.zach.domain.User;
import com.zach.dto.ResponseResult;
import com.zach.dto.UserDTO;
import com.zach.repo.UserRepository;
import com.zach.service.UserService;
import com.zach.utils.JWTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.zach.utils.RedisConst.CACHE_USER_TTL;
import static com.zach.utils.RedisConst.LOGIN_USER_KEY;

@Service
public class UserServiceImpl implements UserService {


    final private AuthenticationManager authenticationManager;

    @Resource
    RedisTemplate redisTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        redisTemplate.opsForValue().set(LOGIN_USER_KEY + userId, userDTO, CACHE_USER_TTL, TimeUnit.DAYS);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", userDTO);
        userMap.put("token", token);

        // return token along with user info
        return new ResponseResult(200, "Login successful", userMap);
    }

    @Override
    public ResponseResult register(User user) {
        Optional<User> userDB = userRepository.findByUsername(user.getUsername());
        userDB.ifPresent(u -> {
            throw new RuntimeException("username: " + u.getUsername() + " already exists");
        });

        // encode password
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);

        // save user and generate token based on ID
        User newUser = userRepository.save(user);
        String userId = newUser.getId().toString();
        String token = JWTUtils.createJWT(userId);

        // Exclude unnecessary fields
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(newUser, userDTO);

        redisTemplate.opsForValue().set(LOGIN_USER_KEY + userId, userDTO, CACHE_USER_TTL, TimeUnit.DAYS);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", userDTO);
        userMap.put("token", token);

        // return token along with user info
        return new ResponseResult(201, "Register successful", userMap);
    }

    @Override
    public ResponseResult logout() {
        // get userId from SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = (UserDTO) authentication.getPrincipal();
        String userId = user.getId().toString();
        // delete value from redis
        String redisKey = LOGIN_USER_KEY + userId;
        redisTemplate.delete(redisKey);
        return new ResponseResult(200, "Logout successful");
    }
}
