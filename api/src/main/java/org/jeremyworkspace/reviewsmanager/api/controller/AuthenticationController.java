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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response){
        // Adding expired RefreshToken to Cookie since we use http only, the front end cannot delete the cookie on itself.
        Cookie cookie = this.jwtService.getExpiredRefreshTokenCookie();
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    /**
     * When we receive the Refresh Token, we try to get the user in the persistent model so that deleted user inbetween won't be able to use the application anymore.
     * @param request
     * @param response
     */
    @GetMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookie =
                cookies == null ?
                        null : Arrays.stream(cookies).filter(c -> c.getName().equals(this.jwtConfig.getRefreshTokenCookieName())).findFirst();

        if(cookie == null ){
            response.setHeader("error", "The refresh token is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        try{
            String token = cookie.get().getValue();
            Jws<Claims> claims = this.jwtService.validateToken(token);
            Claims body = claims.getBody();
            User u = this.userService.getUserByUsername(body.getSubject()).orElseThrow();

            String newAuthorizationToken = this.jwtService.createAuthorizationToken(u);

            response.addHeader("Authorization", "Bearer " + newAuthorizationToken);

        } catch (JwtException ex){
            response.setHeader("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }
}
