package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

	private SupplierRepository supplierRepository;

	@Autowired
	public SupplierService(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	public List<Supplier> obtenerTodos() {
		return supplierRepository.findAll();
	}

	public Optional<Supplier> obtenerPorId(Long id) {
		return supplierRepository.findById(id);
	}

	public Supplier guardar(Supplier proveedor) {
		return supplierRepository.save(proveedor);
	}

	public boolean eliminar(Long id) {
		Optional<Supplier> supplierToDelete = supplierRepository.findById(id);
		if(supplierToDelete.isPresent()){
			supplierRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
