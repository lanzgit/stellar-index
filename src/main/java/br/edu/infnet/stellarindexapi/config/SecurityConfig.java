package br.edu.infnet.stellarindexapi.config;

import br.edu.infnet.stellarindexapi.security.FiltroAutenticacao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final FiltroAutenticacao filtroAutenticacao;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configure(http))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers("/api/nasa/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/**")
                    .hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.PUT, "/api/**")
                    .hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.PATCH, "/api/**")
                    .hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(filtroAutenticacao, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
