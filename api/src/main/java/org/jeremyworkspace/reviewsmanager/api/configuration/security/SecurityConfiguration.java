package org.jeremyworkspace.reviewsmanager.api.configuration.security;


import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtConfig;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtService;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtTokenVerifierFilter;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfiguration {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final ApplicationContext applicationContext;

    @Autowired
    public SecurityConfiguration(JwtService jwtService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, ApplicationContext applicationContext) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.applicationContext = applicationContext;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Preparing Authentication Provider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(provider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();

        // Preparing URLS
        final String LOGIN_ENDPOINT = "/api/login";
        final String[] FRONT_END_URLS = {
                "/",
                "/inscription",
                "/connexion",
                "/mentionslegales",
                "/mesclassements",
                "/gestionclassements",
                "/editerclassement/*",
                "/consulterclassement/*",
                "/deconnexion",
                "/assets/**",
                "/static/**",
                "/version"
        };
        final String[] API_PUBLIC_GET_ENDPOINTS = {
                "/api/refreshToken",
                "/api/users/count",
                "/api/logout"
        };
        final String[] API_PUBLIC_POST_ENDPOINTS = {
                "/api/users", // registration endpoint
                LOGIN_ENDPOINT  // login endpoint
        };

        http
                //.requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager, jwtService, LOGIN_ENDPOINT))
                .addFilterAfter(this.applicationContext.getBean(JwtTokenVerifierFilter.class), JwtUsernameAndPasswordAuthenticationFilter.class)

                // Authorizations
                .authorizeRequests()
                    // Public URL
                    .antMatchers(HttpMethod.GET, FRONT_END_URLS).permitAll()
                    .antMatchers(HttpMethod.GET, API_PUBLIC_GET_ENDPOINTS).permitAll()
                    .antMatchers(HttpMethod.POST, API_PUBLIC_POST_ENDPOINTS).permitAll()
                    // Others URLs require authentication by default
                    .anyRequest().authenticated()

                .and()
                .authenticationManager(authenticationManager);

        return http.build();
    }

}
