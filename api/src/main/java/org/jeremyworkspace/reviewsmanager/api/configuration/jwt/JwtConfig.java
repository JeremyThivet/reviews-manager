package org.jeremyworkspace.reviewsmanager.api.configuration.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.net.http.HttpHeaders;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer authTokenExpireAfterMinutes;
    private Integer refreshTokenExpireAfterDays;
    private String refreshTokenCookieName;
    private final String authorizationHeader = "Authorization";

    @Bean
    public SecretKey secretKey(){
        return Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }
}