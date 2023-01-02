package com.zach.utils;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Signature;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JWTUtils {

    public static final Long JWT_TTL = 60 * 60 *1000L;// 60 * 60 *1000  one hour

    public static final String JWT_KEY = "zach";


    public static String createJWT( String payload, Long ttlMillis){
        JwtBuilder jwtBuilder = getJWTBuilder(payload, ttlMillis, getUUID());
        return jwtBuilder.compact();
    }

    public static String createJWT(String payload) {
        JwtBuilder builder = getJWTBuilder(payload, null, getUUID());
        return builder.compact();
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = getKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    public static SecretKey getKey(){
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    public static JwtBuilder getJWTBuilder(String payload, Long ttlMillis, String uuid) {
        SignatureAlgorithm algo = SignatureAlgorithm.HS256;
        SecretKey key = getKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        return Jwts.builder()
                .setId(uuid)
                .setSubject(payload)
                .setIssuer("zh")
                .setIssuedAt(now)
                .signWith(algo,key)
                .setExpiration(expDate);
    }



}
