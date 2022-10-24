package org.jeremyworkspace.reviewsmanager.api.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.jeremyworkspace.reviewsmanager.api.auth.UserAuthenticationService;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtConfig;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtService;
import org.jeremyworkspace.reviewsmanager.api.model.dto.UserDto;
import org.jeremyworkspace.reviewsmanager.api.model.response.UserResponse;
import org.jeremyworkspace.reviewsmanager.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        // Adding RefreshToken to Cookie
        Cookie cookie = this.jwtService.getExpiredRefreshTokenCookie();
        response.addCookie(cookie);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        if(cookies == null ){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error", "The refresh token is missing");
            return;
        }

        Optional<Cookie> cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(this.jwtConfig.getRefreshTokenCookieName())).findFirst();

        if(cookie.isEmpty()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error", "The refresh token is missing");
            return;
        }

        String token = cookie.get().getValue();

        try{

            Jws<Claims> claims = this.jwtService.validateToken(token);
            Claims body = claims.getBody();
            String username = body.getSubject();

            String newAuthorizationToken = this.jwtService.createAuthorizationToken(username, new HashSet<>());

            response.addHeader("Authorization", "Bearer " + newAuthorizationToken);

        } catch (JwtException ex){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error", ex.getMessage());
            return;
        }
    }
}
