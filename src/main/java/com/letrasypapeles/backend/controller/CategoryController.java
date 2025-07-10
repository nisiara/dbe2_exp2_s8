package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Category;
import com.letrasypapeles.backend.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Categoría", description = "Operaciones relacionadas con las categorías")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<List<Category>> getAllCategories(){
		List<Category> categories = categoryService.obtenerTodas();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
		return categoryService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Category> createCategory(@RequestBody Category category){
		Category newCategory = categoryService.guardar(category);
		return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{categoryId}")
	ResponseEntity<String> delete(@PathVariable Long categoryId) {
		boolean isDeleted = categoryService.eliminar(categoryId);
		if(isDeleted){
			return new ResponseEntity<>("Categoria borrada exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
