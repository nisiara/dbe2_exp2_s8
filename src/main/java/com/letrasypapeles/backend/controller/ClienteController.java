package com.letrasypapeles.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rol Cliente", description = "Operaciones relacionadas con el usuario con rol 'Cliente'")
public class ClienteController {

	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("cliente")
	public ResponseEntity<String> cliente() {
		return ResponseEntity.ok("Eres el cliente");
	}
}
