package com.letrasypapeles.backend.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.UserDTO;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.RoleRepository;
import com.letrasypapeles.backend.repository.UserRepository;

@Service
public class AuthenticationService {

  private UserRepository userRepository;
  private RoleRepository roleRepository;

  public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  
  public User createUser(UserDTO userDTO) {
    Set<Role> roles = userDTO.getRoles().stream()
        .map(roleName -> roleRepository.findByRoleName(roleName)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)))
        .collect(Collectors.toSet());
    
    User user = User.builder()
      .username(userDTO.getUsername())
      .password(userDTO.getPassword())
      .name(userDTO.getName())
      .email(userDTO.getEmail())
      .roles(roles)
      .build();
        
    return userRepository.save(user);
  }
}
