package org.jeremyworkspace.reviewsmanager.api.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.jeremyworkspace.reviewsmanager.api.auth.UserAuthenticationService;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtConfig;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtService;
import org.jeremyworkspace.reviewsmanager.api.model.User;
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

    /**
     * WHen we receive the Refresh Token, we try to get the user in the persistent model so that deleted user inbetween won't be able to use the application anymore.
     * @param request
     * @param response
     */
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
            User u = this.userService.getUserByUsername(body.getSubject()).orElseThrow();

            String newAuthorizationToken = this.jwtService.createAuthorizationToken(u);

            response.addHeader("Authorization", "Bearer " + newAuthorizationToken);

        } catch (JwtException ex){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error", ex.getMessage());
            return;
        }
    }
}
