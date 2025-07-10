package com.letrasypapeles.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private JwtAuthEntryPoint authEntryPoint;
	@Autowired
	public SecurityConfig(CustomUserDetailService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
		this.authEntryPoint = authEntryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exceptions ->
				exceptions.authenticationEntryPoint(authEntryPoint)
			)
			.sessionManagement(sessions ->
				sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			.authorizeHttpRequests(authz -> authz
				// Permite acceso a las URLs de Swagger/OpenAPI
				.requestMatchers("/v3/api-docs/**").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll()
				.requestMatchers("/swagger-ui.html").permitAll() // Por si acaso
				.requestMatchers("/webjars/**").permitAll() // Para los recursos estáticos de Swagger UI
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/empleado").hasRole("EMPLEADO")
				.requestMatchers("/api/role/**").hasRole("GERENTE")
				.requestMatchers("/cliente").hasRole("CLIENTE")
				// .anyRequest().authenticated()
				.anyRequest().permitAll()

			);
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


		return http.build();

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}


}
