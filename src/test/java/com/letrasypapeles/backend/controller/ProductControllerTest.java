package com.letrasypapeles.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	controllers = ProductController.class,
	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
)
public class ProductControllerTest {
  @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private Product producto;

  @BeforeEach
	public void setUp() {
		producto = new Product();
		producto.setId(1L);
    producto.setName("Nombre");

	}

  @Test
	public void testGetProductList() throws Exception {
		when(productService.obtenerTodos()).thenReturn(Collections.singletonList(producto));
		mockMvc.perform(get("/api/product"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(producto))));
	}

  @Test
	public void testGetProductoById() throws Exception {
		when(productService.obtenerPorId(1L)).thenReturn(Optional.of(producto));
		mockMvc.perform(get("/api/product/1"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(producto)));
	}

  @Test
	public void testCreateproducto() throws Exception {
		when(productService.guardar(producto)).thenReturn(producto);
		mockMvc.perform(post("/api/product/create")
				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
				.content(objectMapper.writeValueAsString(producto)))
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(producto)));
	}

  @Test
	public void testUpdateproducto() throws Exception {
		when(productService.actualizar(eq(1L), any(Product.class))).thenReturn(producto);
		mockMvc.perform(put("/api/product/update/1")
			.contentType(String.valueOf(MediaType.APPLICATION_JSON))
			.content(objectMapper.writeValueAsString(producto)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(producto)));
	}


  @Test
	public void testDeleteproductoSuccessTest() throws Exception {
		Long productoId = 1L;
		
		when(productService.eliminar(productoId)).thenReturn(true);
		mockMvc.perform(delete("/api/product/delete/{productoId}", productoId))
			.andExpect(status().isOk())
			.andExpect(content().string("Producto eliminado exitosamente"));
		
		verify(productService, times(1)).eliminar(productoId);
	}

	@Test
	public void testDeleteproductoNotFoundTest() throws Exception {
		Long productoId = 99L;
		
		when(productService.eliminar(productoId)).thenReturn(false);
		mockMvc.perform(delete("/api/product/delete/{productoId}", productoId))
			.andExpect(status().isNotFound()); 
		
		verify(productService, times(1)).eliminar(productoId);
	}
  
  
}
