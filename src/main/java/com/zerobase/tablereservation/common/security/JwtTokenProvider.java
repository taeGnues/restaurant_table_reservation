package com.zerobase.tablereservation.common.security;

import com.zerobase.tablereservation.src.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private final AuthService authService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    토큰 생성
     */
    public String generateToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME); // 만료일

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(this.getSigningKey())
                .compact();
    }

    /*
    토큰으로부터 UserDetail 가져와서 인증 정보 전달
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = authService.loadUserByUsername(getUsername(token));
        log.info("{} -> ", getUsername(token));
        log.info("{} -> ", userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        if(!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    private String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

}
