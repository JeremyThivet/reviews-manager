package org.jeremyworkspace.reviewsmanager.api.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    private final UserService userService;

    public JwtService(JwtConfig jwtConfig, UserService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
        this.secretKey = this.jwtConfig.secretKey();
    }

    public String createAuthorizationToken(User user){
        Date now = new Date();
        HashMap<String, String> claims = new HashMap<String, String>();
        claims.put("id", user.getId().toString());
        claims.put("sub", user.getUsername());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plusSeconds(60 * this.jwtConfig.getAuthTokenExpireAfterMinutes())))
                //.setExpiration(Date.from(now.toInstant().plusSeconds(60)))
                .signWith(secretKey)
                .compact();

        // Saving last login
        user.setLastConnection(new Date());
        this.userService.updateUser(user);

        return token;
    }

    public String createRefreshToken(User user){
        Date now = new Date();
        HashMap<String, String> claims = new HashMap<String, String>();
        claims.put("id", user.getId().toString());
        claims.put("sub", user.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date.from(now.toInstant().plusSeconds(this.getRefreshTokenDurationInSeconds())))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createRefreshTokenCookie(User user){
        Cookie cookie = new Cookie(this.jwtConfig.getRefreshTokenCookieName(), this.createRefreshToken(user));
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
