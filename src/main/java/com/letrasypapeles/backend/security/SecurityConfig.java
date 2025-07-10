package com.letrasypapeles.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
    
    httpSecurity
			.csrf(csrf -> csrf.disable())
			
			.sessionManagement(sessions ->
				sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			.authorizeHttpRequests(authentication -> authentication
				.requestMatchers("/api/auth/login").permitAll()
				.anyRequest().authenticated()
			)
      
      .httpBasic(Customizer.withDefaults());

    return httpSecurity.build();
  }

  @Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

@Bean
  UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
      User.withUsername("todopoderoso")
        .password(passwordEncoder.encode("666")) // Encode the password here!
        .roles("ADMIN") // Assign a role, e.g., ADMIN, USER
        .build());
    return manager;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  
}
