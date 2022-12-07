package org.jeremyworkspace.reviewsmanager.api.configuration.security;


import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtConfig;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtService;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtTokenVerifierFilter;
import org.jeremyworkspace.reviewsmanager.api.configuration.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(JwtService jwtService, JwtConfig jwtConfig, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
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
        String loginUrl = "/api/login";
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
                "/api/users"
        };

        http
                //.requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                /*.authorizeRequests()
                .antMatchers("/**").permitAll();*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager, jwtService, loginUrl))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtService, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)

                // Authorizations
                .authorizeRequests()
                // Public URL
                .antMatchers(HttpMethod.POST, loginUrl).permitAll()
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
