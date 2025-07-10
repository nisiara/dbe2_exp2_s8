package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventory;
import com.letrasypapeles.backend.service.InventoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventoryController {

	private InventoryService inventoryService;

	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@GetMapping
	public ResponseEntity<List<Inventory>> obtenerTodos() {
		List<Inventory> inventarios = inventoryService.obtenerTodos();
		return ResponseEntity.ok(inventarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Inventory> obtenerPorId(@PathVariable Long id) {
		return inventoryService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Inventory> crearInventario(@RequestBody Inventory inventario) {
		Inventory newInventory = inventoryService.guardar(inventario);
		return new ResponseEntity<>(newInventory, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{inventoryId}")
	ResponseEntity<String> delete(@PathVariable Long inventoryId) {
		boolean isDeleted = inventoryService.eliminar(inventoryId);
		if(isDeleted){
			return new ResponseEntity<>("Inventario borrado exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
