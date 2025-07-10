package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Category;
import com.letrasypapeles.backend.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	controllers = CategoryController.class,
	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService categoryService;

	@Autowired
	private ObjectMapper objectMapper;

	private Category category;

	@BeforeEach
	public void setUp() {
		category = new Category();
		category.setId(1L);
		category.setName("Categoria");
	}

	@Test
	public void testGetCategoryList() throws Exception {
		when(categoryService.obtenerTodas()).thenReturn(Collections.singletonList(category));
		mockMvc.perform(get("/api/category"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(category))));
	}

	@Test
	public void testGetCategoryBId() throws Exception {
		when(categoryService.obtenerPorId(1L)).thenReturn(Optional.of(category));
		mockMvc.perform(get("/api/category/1"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(category)));
	}

	@Test
	public void testCreatecategory() throws Exception {
		when(categoryService.guardar(category)).thenReturn(category);
		mockMvc.perform(post("/api/category/create")
				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
				.content(objectMapper.writeValueAsString(category)))
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(category)));
	}

	@Test
	public void testDeletecategorySuccessTest() throws Exception {
		Long sucursalId = 1L;
		
		when(categoryService.eliminar(sucursalId)).thenReturn(true);
		mockMvc.perform(delete("/api/category/delete/{sucursalId}", sucursalId))
			.andExpect(status().isOk())
			.andExpect(content().string("Categoria borrada exitosamente"));
		
		verify(categoryService, times(1)).eliminar(sucursalId);
	}

	@Test
	void testDeletecategoryNotFoundTest() throws Exception {
		Long categoriaId = 99L;
		
		when(categoryService.eliminar(categoriaId)).thenReturn(false);
		mockMvc.perform(delete("/api/category/delete/{categoriaId}", categoriaId))
			.andExpect(status().isNotFound()); 
		
		verify(categoryService, times(1)).eliminar(categoriaId);
	}
}
