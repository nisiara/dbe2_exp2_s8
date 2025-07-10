package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	private ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> obtenerTodos() {
		return productRepository.findAll();
	}

	public Optional<Product> obtenerPorId(Long id) {
		return productRepository.findById(id);
	}

	public Product guardar(Product product) {
    return productRepository.save(product);
  }

	public Product actualizar(Long id, Product producto) {
		if(productRepository.existsById(id)){
			producto.setId(id);
			return productRepository.save(producto);
		}   else {
			return null;
		}
	}

	public boolean eliminar(Long id) {
		Optional<Product> productToDelete = productRepository.findById(id);
		if(productToDelete.isPresent()){
			productRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
