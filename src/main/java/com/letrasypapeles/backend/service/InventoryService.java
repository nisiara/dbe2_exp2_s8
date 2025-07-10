package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventory;
import com.letrasypapeles.backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
	private InventoryRepository inventoryRepository;

	@Autowired
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public List<Inventory> obtenerTodos() {
		return inventoryRepository.findAll();
	}

	public Optional<Inventory> obtenerPorId(Long id) {
		return inventoryRepository.findById(id);
	}

	public Inventory guardar(Inventory inventario) {
		return inventoryRepository.save(inventario);
	}

	public boolean eliminar(Long id) {
		Optional<Inventory> inventoryToDelete = inventoryRepository.findById(id);
		if(inventoryToDelete.isPresent()){
			inventoryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	
}
