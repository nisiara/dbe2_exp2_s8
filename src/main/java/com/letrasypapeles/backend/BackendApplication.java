package com.letrasypapeles.backend;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.letrasypapeles.backend.repository.UserRepository;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);}

		@Autowired
		PasswordEncoder passwordEncoder;

		@Autowired
		UserRepository userRepository;

		 @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {

					User userNicolas = User.builder()
						.name("nicolas")
						.username("nicolas")
						.password(passwordEncoder.encode("1234"))
						.email("correo@email.com")
						.roles(Set.of(
							Role.builder()
								.roleName(ERole.valueOf(ERole.ADMIN.name()))
								.build()
						))
						.build();
					userRepository.save(userNicolas);
		
					User userJavier = User.builder()
						.name("javier")
						.username("javier")
						.password(passwordEncoder.encode("1234"))
						.email("correo@email.com")
						.roles(Set.of(
							Role.builder()
								.roleName(ERole.valueOf(ERole.DEVELOPER.name()))
								.build()
						))
						.build();
					userRepository.save(userJavier);
			
					User userOtro = User.builder()
						.name("otro")
						.username("otro")
						.password(passwordEncoder.encode("1234"))
						.email("correo@email.com")
						.roles(Set.of(
							Role.builder()
								.roleName(ERole.valueOf(ERole.COMPRA.name()))
								.build()
						))
						.build();
					userRepository.save(userOtro);
				};

			};

}
