package com.aliu.be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aliu.be.exception.ErrorCode;
import com.aliu.be.exception.TokenException;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Tạo, đọc và xác thực Access Token/Refresh Token bằng JJWT 0.12.x.
 */
@Component
public class JwtTokenProvider {

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final SecretKey secretKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;
    private final String issuer;
    private final Clock clock;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String base64Secret,
            @Value("${app.jwt.access-token-expiration:15m}") Duration accessTokenExpiration,
            @Value("${app.jwt.refresh-token-expiration:7d}") Duration refreshTokenExpiration,
            @Value("${app.jwt.issuer:chinese-learning-api}") String issuer,
            Clock clock
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("app.jwt.secret phải có tối thiểu 256 bit sau khi Base64 decode");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.issuer = issuer;
        this.clock = clock;
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted()
                .toList();

        return buildToken(
                userDetails.getUsername(),
                userDetails.getId(),
                ACCESS_TOKEN_TYPE,
                accessTokenExpiration,
                authorities
        );
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return buildToken(
                userDetails.getUsername(),
                userDetails.getId(),
                REFRESH_TOKEN_TYPE,
                refreshTokenExpiration,
                List.of()
        );
    }

    private String buildToken(
            String subject,
            Long userId,
            String tokenType,
            Duration expiration,
            List<String> authorities
    ) {
        Instant now = clock.instant();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(subject)
                .claim("user_id", userId)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .claim(AUTHORITIES_CLAIM, authorities)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String getTokenId(String token) {
        return parseClaims(token).getId();
    }

    public Instant getExpiration(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }

    public void validateAccessToken(String token, UserDetails userDetails) {
        Claims claims = parseClaims(token);
        validateCommonClaims(claims, userDetails, ACCESS_TOKEN_TYPE);
    }

    public void validateRefreshToken(String token, UserDetails userDetails) {
        Claims claims = parseClaims(token);
        validateCommonClaims(claims, userDetails, REFRESH_TOKEN_TYPE);
    }

    private void validateCommonClaims(
            Claims claims,
            UserDetails userDetails,
            String requiredTokenType
    ) {
        if (!issuer.equals(claims.getIssuer())) {
            throw new TokenException(ErrorCode.INVALID_TOKEN, "JWT issuer không hợp lệ");
        }
        if (!requiredTokenType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
            throw new TokenException(ErrorCode.INVALID_TOKEN, "Sai loại token");
        }
        if (!userDetails.getUsername().equals(claims.getSubject())) {
            throw new TokenException(ErrorCode.INVALID_TOKEN, "Token không thuộc người dùng hiện tại");
        }
        if (!userDetails.isEnabled() || !userDetails.isAccountNonLocked()) {
            throw new TokenException(ErrorCode.UNAUTHORIZED, "Tài khoản không còn hoạt động");
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException exception) {
            throw new TokenException(ErrorCode.EXPIRED_TOKEN, "Token đã hết hạn", exception);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new TokenException(ErrorCode.INVALID_TOKEN, "Token không hợp lệ", exception);
        }
    }
}
