package co.com.makers.api.jwt;

import co.com.makers.api.jwt.Exception.JwtValidationException;
import co.com.makers.api.jwt.Exception.TokenExpiredException;
import co.com.makers.model.user.valueobject.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

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

        public JwtService(
                @Value("${jwt.secret}") String secret,
                @Value("${jwt.expiration-ms}") long expirationMs,
                @Value("${jwt.issuer}") String issuer
        ) {
            this.key = Keys.hmacShaKeyFor(secret.getBytes());
            this.expirationMs = expirationMs;
            this.issuer = issuer;
        }

        public String generateAccessToken(String userId, Role role){
            Instant now = Instant.now();
            return Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setSubject(userId)
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

        public Claims getTokenClaims(String token) {
            if (token == null || token.trim().isEmpty()) {
                throw new JwtValidationException("Token cannot be null or empty");
            }
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .requireIssuer(issuer)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                validateTokenClaims(claims);
                return claims;

            } catch (ExpiredJwtException ex) {
                log.warn("Token has expired: {}", ex.getMessage());
                throw new TokenExpiredException("Token has expired at: " + ex.getClaims().getExpiration());

            } catch (UnsupportedJwtException ex) {
                log.error("Unsupported JWT token: {}", ex.getMessage());
                throw new JwtValidationException("Unsupported JWT token");

            } catch (MalformedJwtException ex) {
                log.error("Malformed JWT token: {}", ex.getMessage());
                throw new JwtValidationException("Malformed JWT token");

            } catch (io.jsonwebtoken.security.SignatureException ex) {
                log.error("Invalid JWT signature: {}", ex.getMessage());
                throw new JwtValidationException("Invalid JWT signature");

            } catch (IllegalArgumentException ex) {
                log.error("JWT token compact of handler are invalid: {}", ex.getMessage());
                throw new JwtValidationException("Invalid JWT token");

            } catch (Exception ex) {
                log.error("Unexpected error validating JWT token: {}", ex.getMessage(), ex);
                throw new JwtValidationException("Token validation failed: " + ex.getMessage());
            }
        }

        private void validateTokenClaims(Claims claims) {
            if (claims.getSubject() == null || claims.getSubject().trim().isEmpty()) {
                throw new JwtValidationException("Token missing required subject claim");
            }

            if (claims.getIssuedAt() == null) {
                throw new JwtValidationException("Token missing issued at claim");
            }

            if (claims.getIssuedAt().after(new Date())) {
                throw new JwtValidationException("Token issued in the future");
            }

            if (claims.get("nbf") != null) {
                Date notBefore = new Date(((Number) claims.get("nbf")).longValue() * 1000);
                if (notBefore.after(new Date())) {
                    throw new JwtValidationException("Token not yet valid");
                }
            }
        }

        public long getRemainingExpirationTime(Claims claims) {
            Date expiration = claims.getExpiration();
            return Math.max(0, expiration.getTime() - System.currentTimeMillis());
        }
    }