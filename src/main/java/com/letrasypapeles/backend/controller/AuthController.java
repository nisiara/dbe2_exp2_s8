package com.letrasypapeles.backend.controller;


import com.letrasypapeles.backend.dto.RegisterDTO;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;

import com.letrasypapeles.backend.repository.UserRepository;
// import com.letrasypapeles.backend.security.JwtGenerator;
import com.letrasypapeles.backend.service.UserService;


import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarion'")
public class AuthController {
	// private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	// private final RoleRepository roleRepository;
	private UserService userService;
	private final PasswordEncoder passwordEncoder;
	// private final JwtGenerator jwtGenerator;

	@Autowired
	// public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator) {
	public AuthController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
		// this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		// this.roleRepository = roleRepository;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		// this.jwtGenerator = jwtGenerator;
	}

	// @PostMapping("login")
	// public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
	// 	Authentication authentication = authenticationManager.authenticate(
	// 		new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
	// 		SecurityContextHolder.getContext().setAuthentication(authentication);
	// 		String token = jwtGenerator.generateToken(authentication);
	// 		return ResponseEntity.ok(new AuthResponseDTO(token));
	// }

	@PostMapping("registro")
	public ResponseEntity<User> registro(@RequestBody RegisterDTO registerDTO) {
		// if(userRepository.existsByUsername(registerDTO.getUsername())) {
		// 	return ResponseEntity.badRequest().body("El usuario ya existe");
		// }

		Set<Role> roles = registerDTO.getRoles().stream()
			.map(role -> Role.builder()
				.roleName(ERole.valueOf(role))
				.build())
			.collect(Collectors.toSet());
		
		User user = new User();
		user.setUsername(registerDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		user.setPassword(registerDTO.getPassword());
		user.setName(registerDTO.getName());
		user.setEmail(registerDTO.getEmail());
		user.setRoles(roles);

		userRepository.save(user);

		return new ResponseEntity<>(user, HttpStatus.CREATED);

	}

	
	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> delete(@PathVariable Long id){
		boolean isDeleted = userService.eliminar(id);
		if(isDeleted){
			Map<String, String> response = new HashMap<>();
			response.put("message", "Usuario con id " + id +" eliminado exitosamente");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}
