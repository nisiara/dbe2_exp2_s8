package com.letrasypapeles.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.ClientDTO;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClientRepository;
import com.letrasypapeles.backend.repository.RoleRepository;

@Service
public class ClientService {
  private ClientRepository clientRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public ClientService(ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.clientRepository = clientRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  	public List<Client> getClients() {
      List<Client> clients = clientRepository.findAll();
        
      // clients.forEach(client -> {
      //   Set<Role> filteredRoles = client.getRoles().stream()
      //     .filter(role -> role.getRoleName() == ERole.COMPRA)
      //     .collect(Collectors.toSet());
      //     client.setRoles(filteredRoles);
      // });
        
        return clients;
		
	}

	public Optional<Client> getById(Long id) {
    return clientRepository.findById(id);
      // .map(client -> {
      //     Set<Role> filteredRoles = client.getRoles().stream()
      //         .filter(role -> role.getRoleName() == ERole.COMPRA)
      //         .collect(Collectors.toSet());
      //     client.setRoles(filteredRoles);
      //     return client;
      // });
	}

  public Client create(ClientDTO clientDTO) {
    // Validar email único
    if (clientRepository.existsByEmail(clientDTO.getEmail())) {
      throw new IllegalArgumentException("El email ya está registrado");
    }
      
      
    Client client = new Client();
    client.setName(clientDTO.getName());
    client.setEmail(clientDTO.getEmail());
    client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
    client.setFidelityPoints(clientDTO.getFidelityPoints());
    
    // Asignar rol COMPRA
    // Role compraRole = roleRepository.findByRoleName(ERole.COMPRA)
    //   .orElseGet(() -> {
    //     Role newRole = new Role();
    //     newRole.setRoleName(ERole.COMPRA);
    //     return roleRepository.save(newRole);
    //   });
    
    // client.addRole(compraRole);

    return clientRepository.save(client);

  }

  public boolean delete(Long id) {
    Optional<Client> clientToDelete = clientRepository.findById(id);
    if(clientToDelete.isPresent()){
      clientRepository.deleteById(id);
      return true;
    }
    return false;
	}

	public Client update(Long id, Client client) {
		if(clientRepository.existsById(id)){
			client.setId(id);
			return clientRepository.save(client);
		}   else {
			return null;
		}
	}
}
