package com.myKeyCons.tasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf((csrf) ->  csrf
            .ignoringRequestMatchers("/**")
        )
        .authorizeHttpRequests((authorize) ->
          authorize.anyRequest()
          .authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults());;
    return httpSecurity.build();
  }

  @Bean
  public UserDetailsService users() {
    User.UserBuilder users = User.withDefaultPasswordEncoder();
    UserDetails user = users
        .username("user")
        .password("password")
        .roles("USER")
        .build();
    UserDetails user2 = users
        .username("user2")
        .password("password")
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(user, user2);
  }
}
