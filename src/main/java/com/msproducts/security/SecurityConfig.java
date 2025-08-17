package com.msproducts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Handlers definidos como variables locales (evita Sonar S3305)
    AuthenticationEntryPoint unauthorized = (req, res, ex) -> {
      res.setStatus(401);
      res.setContentType("application/json");
      res.getWriter().write("{\"error\":\"unauthorized\"}");
    };

    AccessDeniedHandler forbidden = (req, res, ex) -> {
      res.setStatus(403);
      res.setContentType("application/json");
      res.getWriter().write("{\"error\":\"forbidden\"}");
    };

    http
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(eh -> eh
          .authenticationEntryPoint(unauthorized)
          .accessDeniedHandler(forbidden))
      .authorizeHttpRequests(auth -> auth
        // Swagger abierto
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/actuator/**").permitAll()
        // Lectura: CLIENT o ADMIN
        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "CLIENT")
        // Escritura: sólo ADMIN
        .requestMatchers(HttpMethod.POST,   "/api/products/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT,    "/api/products/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      // sin httpBasic ni formLogin
      .formLogin(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable);

    // Filtro JWT antes del UsernamePasswordAuthenticationFilter
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
