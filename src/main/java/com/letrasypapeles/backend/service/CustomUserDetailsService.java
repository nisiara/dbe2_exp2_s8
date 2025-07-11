package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		return new org.springframework.security.core.userdetails.User(
			user.getUsername(),
			user.getPassword(),
      true, // accountNonExpired
      true, // accountNonLocked
      true, // credentialsNonExpired
      true, // enabled
      
      user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()))
        .toList()
      
		);
  }

}








