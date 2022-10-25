package org.jeremyworkspace.reviewsmanager.api.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.model.response.UserResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private ObjectMapper objectMapper;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, String loginUrl) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.setFilterProcessesUrl(loginUrl);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            User credentials = objectMapper.readValue(request.getInputStream(), User.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException ex){
            throw new RuntimeException("Could not read request " + ex);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // Retrieving user logged in.
        User u = (User) authResult.getPrincipal();

        // Adding AccessToken to Headers
        String authorizationToken = jwtService.createAuthorizationToken(u);
        response.addHeader("Authorization", "Bearer " + authorizationToken);

        // Adding RefreshToken to Cookie
        Cookie cookie = this.jwtService.createRefreshTokenCookie(u);
        response.addCookie(cookie);

        // Adding user to response body
        UserResponse userResponse = new UserResponse(u);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String userJson =  objectMapper.writeValueAsString(userResponse);
        response.getOutputStream().write(userJson.getBytes());

    }
}
