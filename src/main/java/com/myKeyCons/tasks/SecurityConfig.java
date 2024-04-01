package com.myKeyCons.tasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf((csrf) ->  csrf
            .ignoringRequestMatchers("/*")
        )
        .authorizeHttpRequests((authorize) ->
          authorize.anyRequest()
          .permitAll()
        );
    return httpSecurity.build();
  }
  @Bean
  public AuthenticationTrustResolver trustResolver() {
    return new AuthenticationTrustResolver() {

      @Override
      public boolean isRememberMe(final Authentication authentication) {
        return false;
      }

      @Override
      public boolean isAnonymous(final Authentication authentication) {
        return false;
      }
    };
  }
}
