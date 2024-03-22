package dev.ta2khu75.shopapp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.ta2khu75.shopapp.repositories.RepositoryUser;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig// implements WebMvcConfigurer 
{
  private final RepositoryUser repositoryUser;

  // @Override
  // public void addCorsMappings(CorsRegistry registry) {
  //   registry.addMapping("/api/**") // Thay đổi đường dẫn cụ thể của API của bạn
  //       .allowedOrigins("http://localhost:4200") // Thay đổi origin thành địa chỉ của
  //       // ứng dụng Angular của bạn
  //       .allowedMethods("GET", "POST", "PUT", "DELETE");
  // }

  @Bean
  public UserDetailsService userDetailsService() {
    return phoneNumber -> repositoryUser.findByPhoneNumber(phoneNumber)
        .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with phone number:" + phoneNumber));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider provider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
