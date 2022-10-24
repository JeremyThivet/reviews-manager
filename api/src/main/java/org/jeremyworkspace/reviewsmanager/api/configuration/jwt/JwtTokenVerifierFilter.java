package org.jeremyworkspace.reviewsmanager.api.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenVerifierFilter(JwtService jwtService, JwtConfig jwtConfig) {
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(this.jwtConfig.getAuthorizationHeader());

        // If no token, then we continue the filter chain that will block the request if the user tried to reach an authenticated route.
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(this.jwtConfig.getTokenPrefix())){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.split(" ")[1];

        try{

            Jws<Claims> claims = this.jwtService.validateToken(token);
            Claims body = claims.getBody();
            String username = body.getSubject();

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, new HashSet());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException ex){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("error", ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
