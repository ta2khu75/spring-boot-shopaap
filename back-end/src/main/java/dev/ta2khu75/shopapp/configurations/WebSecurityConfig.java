package dev.ta2khu75.shopapp.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;

import dev.ta2khu75.shopapp.filter.JwtTokenFilter;
import dev.ta2khu75.shopapp.models.Role;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig  
{
  private final JwtTokenFilter jwtTokenFilter;
  @Value("${api.prefix}")
  private String apiPrefix;
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(request -> {
          request.requestMatchers(String.format("%s/users/register", apiPrefix),
              String.format("%s/users/login", apiPrefix),
              String.format("%s/users/refresh-token", apiPrefix),
              String.format("%s/health-check", apiPrefix),
              String.format("/%s/actuator/**", apiPrefix),

              // "/api-docs/**",
              // "/swagger-resources",
              // "/swagger-resources/**",
              // "/configuration/ui",
              // "/configuration/security",
              // "/swagger-ui/**",
              // "/swagger-vi.html",
              // "/webjars/swagger-ui/**",
              // "/swagger-ui/index.html",
              "/api-docs",
              "/api-docs/swagger-config",
              "/swagger-ui/swagger-stand.js",
              "/swagger-ui/favicon-16x16.png",
              "/swagger-ui/swagger-initializer.js",
              "/swagger-ui/swagger-ui-standalone-preset.js",
              "/swagger-ui/swagger-ui-bundle.js",
              "/swagger-ui/index.css",
              "/swagger-ui/swagger-ui.css",
              "/swagger-ui/index.html")
              .permitAll()
              .requestMatchers(GET, String.format("%s/comments", apiPrefix)).permitAll()
              .requestMatchers(PUT, String.format("%s/comments/**", apiPrefix)).hasRole(Role.USER)
              .requestMatchers(POST, String.format("%s/comments", apiPrefix)).hasRole(Role.USER)
              .requestMatchers(DELETE, String.format("%s/comments/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

              .requestMatchers(GET, String.format("%s/users", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(GET, String.format("%s/users/blocker-or-enable/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(PUT, String.format("%s/users/reset-password/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(PUT, String.format("%s/users/user-detail", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
              .requestMatchers(POST, String.format("%s/users/details", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
              .requestMatchers(GET, String.format("%s/roles", apiPrefix)).permitAll()
              // .requestMatchers(GET, String.format("%s/products**", apiPrefix)).permitAll()
              .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).permitAll()
              .requestMatchers(GET, String.format("%s/products/images/*", apiPrefix)).permitAll()
              .requestMatchers(GET, String.format("%s/categories", apiPrefix)).permitAll()
              .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

              .requestMatchers(GET, String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
              .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasRole(Role.USER)
              .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
              // .requestMatchers(GET, String.format("%s/orders/get-orders-by-keyword",
              // apiPrefix)).hasRole(Role.ADMIN)

              .requestMatchers(POST, String.format("%s/products/images/*", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(GET, String.format("%s/orderDetails/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
              .requestMatchers(POST, String.format("%s/orderDetails/**", apiPrefix)).hasRole(Role.USER)
              .requestMatchers(PUT, String.format("%s/orderDetails/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(DELETE, String.format("%s/orderDetails/**", apiPrefix)).hasRole(Role.ADMIN)
              .requestMatchers(GET, String.format("%s/coupons/calculate", apiPrefix)).hasRole(Role.USER)

              .anyRequest()
              .authenticated();
        });
    return http.build();
  }
}
