package org.jeremyworkspace.reviewsmanager.api.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = this.jwtConfig.secretKey();
    }

    public String createAuthorizationToken(String username, Collection<? extends GrantedAuthority> authorities){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plusSeconds(60 * this.jwtConfig.getAuthTokenExpireAfterMinutes())))
                //.setExpiration(Date.from(now.toInstant().plusSeconds(60)))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String username){
        // TODO Enregistrer le last login, la dernière fois où le refresh token a été généré en fait.
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plusSeconds(this.getRefreshTokenDurationInSeconds())))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createRefreshTokenCookie(String username){
        Cookie cookie = new Cookie(this.jwtConfig.getRefreshTokenCookieName(), this.createRefreshToken(username));
        // TODO Active la Same Origin et HTTPS
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(this.getRefreshTokenDurationInSeconds());
        return cookie;
    }

    public Cookie getExpiredRefreshTokenCookie(){
        Cookie cookie = new Cookie(this.jwtConfig.getRefreshTokenCookieName(), "");
        // TODO Active la Same Origin et HTTPS
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        return cookie;
    }

    public Jws<Claims> validateToken(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        return claims;
    }

    private int getRefreshTokenDurationInSeconds(){
        return 60 * 60 * 24 * this.jwtConfig.getRefreshTokenExpireAfterDays();
    }
}
