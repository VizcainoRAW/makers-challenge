package co.com.makers.api.jwt;

import co.com.makers.api.jwt.Exception.JwtValidationException;
import co.com.makers.api.jwt.Exception.TokenExpiredException;
import co.com.makers.model.user.valueobject.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;
    private final String issuer;
    private final JwtParser parser;

    public static final String BEARER = "Bearer ";

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs,
                      @Value("${jwt.issuer}") String issuer) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.issuer = issuer;
        this.parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .setAllowedClockSkewSeconds(30)
                .build();
    }

    public Claims getTokenClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new JwtValidationException("Token cannot be null or empty");
        }
        Claims claims;
        try {
            claims = parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("Token has expired");
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Invalid JWT: {}", ex.getMessage());
            throw new JwtValidationException("Invalid JWT token");
        }
        if (claims.getSubject() == null || claims.getSubject().isBlank()) {
            throw new JwtValidationException("Token missing required subject claim");
        }
        return claims;
    }

        public String generateAccessToken(UUID userId, Role role){
            Instant now = Instant.now();
            return Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setSubject(userId.toString())
                    .setIssuer(issuer)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusMillis(expirationMs)))
                    .claim("userId", userId)
                    .claim("role", role.name())
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }

        public Jws<Claims> parseToken(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token);
        }

        public Long getExpirationTimeMs() {
            return this.expirationMs;
        }
    }