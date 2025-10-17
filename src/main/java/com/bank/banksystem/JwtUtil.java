package com.bank.banksystem;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final Key key = Keys.hmacShaKeyFor("YourSuperSecretKeyYourSuperSecretKey".getBytes());
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 2; // 2 hrs

    // generate token
    public static String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // parse token
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // expire checking
    public static boolean isExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}