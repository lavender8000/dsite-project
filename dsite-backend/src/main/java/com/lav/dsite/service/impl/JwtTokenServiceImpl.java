package com.lav.dsite.service.impl;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.lav.dsite.config.JwtConfig;
import com.lav.dsite.entity.User;
import com.lav.dsite.enums.DsiteAuthType;
import com.lav.dsite.enums.JwtClaimKey;
import com.lav.dsite.enums.TokenType;
import com.lav.dsite.service.JwtTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final SecretKey secretkey;
    private final JwtParser jwtParser;
    
    private JwtConfig jwtConfig;

    public JwtTokenServiceImpl(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretkey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret()));
        this.jwtParser = Jwts.parser().verifyWith(secretkey).build();
    }

    @Override
    public String generateAccessToken(User user, DsiteAuthType dsiteAuthType) {

        TokenType tokenType = TokenType.ACCESS;

        String email = user.getEmail();
        String sub = user.getId().toString();
        String nickname = user.getNickName();

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimKey.TOKEN_TYPE.getKey(), tokenType.value());
        claims.put(JwtClaimKey.AUTH_TYPE.getKey(), dsiteAuthType.value());
        claims.put(JwtClaimKey.EMAIL.getKey(), email);
        claims.put(JwtClaimKey.NICKNAME.getKey(), nickname);

        return createToken(claims, sub);

    }

    private String createToken(Map<String, Object> claims, String sub) {

        Long now = Instant.now().toEpochMilli();
        String token = Jwts.builder()
                .subject(sub)
                .issuer(jwtConfig.getIssuer())
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtConfig.getExpirationInMs()))
                .signWith(secretkey)
                .compact();
        return token;

    }

    // 根據舊的 JWT 更新用戶資訊，並返回新的 JWT
    public String updateAccessToken(String jwt, String nickname) {

        Claims claims = parseToken(jwt);

        String newEmail = claims.get(JwtClaimKey.EMAIL.getKey(), String.class);
        String newSub = claims.getSubject();
        String newNickname;
        String newTokenType = claims.get(JwtClaimKey.TOKEN_TYPE.getKey(), String.class);
        String newDsiteAuthType = claims.get(JwtClaimKey.AUTH_TYPE.getKey(), String.class);

        if (nickname != null && !nickname.isEmpty()) {
            newNickname = nickname;
        } else {
            newNickname = claims.get(JwtClaimKey.NICKNAME.getKey(), String.class);
        }

        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put(JwtClaimKey.TOKEN_TYPE.getKey(), newTokenType);
        newClaims.put(JwtClaimKey.AUTH_TYPE.getKey(), newDsiteAuthType);
        newClaims.put(JwtClaimKey.EMAIL.getKey(), newEmail);
        newClaims.put(JwtClaimKey.NICKNAME.getKey(), newNickname);

        return createToken(newClaims, newSub);

    }

    @Override
    public Claims parseToken(String token) {

        return jwtParser.parseSignedClaims(token).getPayload();

    }

    @Override
    public void addJwtToCookie(String token, HttpServletResponse response) {

        Cookie cookie = new Cookie(jwtConfig.getCookieName(), token);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtConfig.getExpirationInMs() / 1000);

        response.addCookie(cookie);

    }

    @Override
    public String getJwtFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtConfig.getCookieName().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;

    }

    @Override
    public void clearJwtFromCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie(jwtConfig.getCookieName(), null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

    }

}
