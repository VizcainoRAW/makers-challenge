package co.com.makers.api.config.authentication;

import co.com.makers.api.jwt.Exception.JwtValidationException;
import co.com.makers.api.jwt.Exception.TokenExpiredException;
import co.com.makers.api.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        Mono<String> token = Mono.justOrEmpty(authentication.getCredentials().toString());

        return Mono.fromCallable(() -> jwtService.getTokenClaims(token.block()))
                .map(claims -> {
                    String userId = claims.getSubject();
                    String role = claims.get("role", String.class);
                    List<GrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    return (Authentication) new UsernamePasswordAuthenticationToken(userId, token, authorities);
                })
                .onErrorMap(JwtValidationException.class, e -> new BadCredentialsException(e.getMessage(), e))
                .onErrorMap(TokenExpiredException.class, e -> new CredentialsExpiredException(e.getMessage(), e));
    }
}