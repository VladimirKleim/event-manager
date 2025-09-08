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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private CustomAuthEntryPoint customAuthEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //for jwt tokens
                .authorizeHttpRequests(auth ->
                           auth.requestMatchers(HttpMethod.POST, "/locations").hasAuthority("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/locations").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/locations/{id}").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/locations/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/locations/{id}").hasAuthority("ADMIN")

                               .requestMatchers(HttpMethod.POST, "/users").permitAll()
                               .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()
                               .requestMatchers(HttpMethod.GET, "/users/{id}").hasAuthority("ADMIN")
                                   .anyRequest().permitAll()
                                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exp -> exp.authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))


                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
