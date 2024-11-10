package com.birdbi.api.security.jwt;

import com.birdbi.api.constant.Result;
import com.birdbi.api.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private final long ACCESS_TOKEN_TIME 	= 72 * 60 * 60 * 1000;
    private final long REFRESH_TOKEN_TIME 	= 720 * 60 * 60 * 1000;

    public JwtTokenProvider(@Value("${jwt.secret:defaultValue}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        //this.key = Keys.hmacShaKeyFor(keyBytes);
        this.key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    // Method to create AccessToken and RefreshToken with user information
    public TokenDto generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Create an Access Token
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // A method to decrypt the JWT token and retrieve the information contained in the token.
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // Get permission information from claims
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // Create a UserDetails object and return Authentication
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Method for validating token information
    public Result validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return Result.SUCCESS;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
            return Result.TOKEN_INVALID;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
            return Result.TOKEN_EXPIRED_TIME;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
            return Result.TOKEN_UNSUPPORTED_JWT;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            return Result.TOKEN_INVALID;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
