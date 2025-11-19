package com.bomi.main.component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    // 개발 단계에서 refreshtoken 관리
    public final HashMap<String, String> refreshHash = new HashMap<>();

    private final Key key;

    private final long accessExpiration;

    private final long refreshExpiration;

    private final AesBytesEncryptor aesBytesEncryptor;

    public JwtTokenProvider(
            @Value("${spring.security.jwt.secret}") String secret,
            @Value("${spring.security.jwt.access-expiration}") String accessExpiration,
            @Value("${spring.security.jwt.refresh-expiration}") String refreshExpiration,
            AesBytesEncryptor aesBytesEncryptor
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessExpiration = Long.parseLong(accessExpiration);
        this.refreshExpiration = Long.parseLong(refreshExpiration);
        this.aesBytesEncryptor = aesBytesEncryptor;
    }

    public String generateAccessToken(String memberUid, String email, String memberName, String role) {

        String memberId = this.encryptId(memberUid);

        Claims claims = Jwts.claims()
                .subject(memberId) // 'sub' 클레임은 보통 고유 식별자(ID)로 설정합니다. (memberId)
                .add("email", email) // 사용자 정의 클레임 추가
                .add("memberName", memberName) // 사용자 정의 클레임 추가
                .add("role", role) // 사용자 정의 클레임 추가
                .build();

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessExpiration))
                .signWith(key)
                .compact();

    }

    public String generateRefreshToken(String memberUid) {

        Claims claims = Jwts.claims()
                .subject(memberUid)
                .build();

        Date now = new Date();

        String refreshToken = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+refreshExpiration))
                .signWith(key)
                .compact();

        refreshHash.put(refreshToken, memberUid);

        return refreshToken;

    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (
                    claims.getSubject() == null || Objects.equals(claims.getSubject(), "")
                    || claims.get("email") == null || claims.get("email").equals("")
                    || claims.get("memberName") == null || claims.get("memberName").equals("")
            ) {
                System.out.println(claims);
                throw new MalformedJwtException("Invalid JWT token");
            }

            return claims;

        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT token");
            throw e;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token");
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token");
            throw e;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty");
            throw e;
        }
    }

    public String encryptId(String id) {
        byte[] encryptedBytes = aesBytesEncryptor.encrypt(id.getBytes(StandardCharsets.UTF_8));
        return Encoders.BASE64URL.encode(encryptedBytes);
    }

    public String decryptId(String encryptedId) {
        byte[] decodedBytes = Decoders.BASE64URL.decode(encryptedId);
        byte[] decryptedBytes = aesBytesEncryptor.decrypt(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
