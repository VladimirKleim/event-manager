package com.kleim.eventmanager.security;

import com.kleim.eventmanager.security.token.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtTokenFilter jwtTokenFilter;
    @Autowired
    private CustomAuthEntryPoint customAuthEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    public SecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //for jwt tokens
                .authorizeHttpRequests(auth ->
                           auth

                                   .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                   .requestMatchers(HttpMethod.POST, "/api/users/auth").permitAll()
                                   .requestMatchers(HttpMethod.POST, "/api/users/auth/refresh").permitAll()
                                   // swagger
                                   .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/openapi.yaml").permitAll()

                                   .requestMatchers(HttpMethod.POST, "/api/location").hasAuthority("ADMIN")
                                   .requestMatchers(HttpMethod.GET, "/api/location/**").hasAnyAuthority("USER", "ADMIN")
                                   .requestMatchers(HttpMethod.DELETE, "/api/location/{id}").hasAuthority("ADMIN")
                                   .requestMatchers(HttpMethod.PUT, "/api/location/{id}").hasAuthority("ADMIN")

                                   .requestMatchers(HttpMethod.POST, "/api/events").hasAnyAuthority("USER", "ADMIN")
                                   .requestMatchers(HttpMethod.GET, "/api/events/**").hasAnyAuthority("USER", "ADMIN")
                                   .requestMatchers(HttpMethod.PUT, "/api/events/{eventId}").hasAnyAuthority("USER", "ADMIN")
                                   .requestMatchers(HttpMethod.DELETE, "/api/events/{eventId}").hasAnyAuthority("USER", "ADMIN")
                                   .requestMatchers(HttpMethod.POST, "/api/events/search").hasAuthority("USER")

                                   .requestMatchers(HttpMethod.POST, "/api/events/registrations/{eventId}").hasAuthority("USER")
                                   .requestMatchers(HttpMethod.DELETE, "/api/events/registrations/cancel/{eventId}").hasAuthority("USER")
                                   .requestMatchers(HttpMethod.GET, "/api/events/registrations/my").hasAuthority("USER")

                                   .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAuthority("ADMIN")

                                   .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exp -> exp.authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
