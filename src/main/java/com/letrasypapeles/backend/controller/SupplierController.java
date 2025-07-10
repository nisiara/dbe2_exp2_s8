package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.service.SupplierService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@Tag(name = "Proveedores", description = "Operaciones relacionadas con los proveedores")
public class SupplierController {

	private SupplierService supplierService;

	@Autowired
	public SupplierController(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	@GetMapping
	public ResponseEntity<List<Supplier>> getAll() {
		List<Supplier> suppliers = supplierService.obtenerTodos();
		return ResponseEntity.ok(suppliers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Supplier> getById(@PathVariable Long id) {
		return supplierService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
		Supplier newSupplier = supplierService.guardar(supplier);
		return new ResponseEntity<>(newSupplier, HttpStatus.CREATED);
	}


	@DeleteMapping("/delete/{supplierId}")
	ResponseEntity<String> delete(@PathVariable Long supplierId) {
		boolean isDeleted = supplierService.eliminar(supplierId);
		if(isDeleted){
			return new ResponseEntity<>("Proveedor eliminado exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
