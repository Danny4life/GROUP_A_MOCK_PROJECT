package com.group_A.MyTodo_App.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final AuthenticationEntryPoint authenticationEntryPoint;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security)throws Exception{
    security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    security.csrf(CsrfConfigurer::disable)
            .authorizeHttpRequests(
                    request ->request
                            .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/auth/**"),
                                    antMatcher(HttpMethod.GET,"/api/v1/**"))
                            .permitAll()
                            .anyRequest()
                            .permitAll()
            )
            .exceptionHandling(exception ->exception
                    .authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults());
    security.authenticationProvider(authenticationProvider);

    return security.build();
  }


}

