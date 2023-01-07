package com.zach.filter;

import com.zach.dto.UserDTO;
import com.zach.utils.JWTUtils;
import com.zach.utils.RedisConst;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.zach.utils.RedisConst.LOGIN_USER_KEY;

@Component
public class JwtTokenVerifier extends OncePerRequestFilter {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        // Bearer token is not provided
        if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer token is present in the header
        String userId;
        try {
            String token = authorizationHeader.split(" ")[1];
            Claims claims = JWTUtils.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token invalid");
        }

        // get user info from redis
        String redisKey = LOGIN_USER_KEY + userId;
        Object userObj = redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(userObj)) {
            throw new RuntimeException("Token expired, please login again");
        }
        UserDTO user = (UserDTO) userObj;

        // store user info in SecurityContextHolder (this will authenticate the user)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
