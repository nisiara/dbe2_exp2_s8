package com.letrasypapeles.backend.controller;

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
import com.letrasypapeles.backend.entity.Inventory;
import com.letrasypapeles.backend.service.InventoryService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	controllers = InventoryController.class,
	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
)
public class InventoryControllerTest {
  @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private InventoryService inventoryService;

	@Autowired
	private ObjectMapper objectMapper;

	private Inventory inventory;

  @BeforeEach
	public void setUp() {
		inventory = new Inventory();
		inventory.setId(1L);
		inventory.setStock(1000);
	}

  @Test
	public void testGetInventoryList() throws Exception {
		when(inventoryService.obtenerTodos()).thenReturn(Collections.singletonList(inventory));
		mockMvc.perform(get("/api/inventory"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(inventory))));
	}

	@Test
	public void testGetInventoryById() throws Exception {
		when(inventoryService.obtenerPorId(1L)).thenReturn(Optional.of(inventory));
		mockMvc.perform(get("/api/inventory/1"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(inventory)));
	}

	@Test
	public void testCreateInventory() throws Exception {
		when(inventoryService.guardar(inventory)).thenReturn(inventory);
		mockMvc.perform(post("/api/inventory/create")
				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
				.content(objectMapper.writeValueAsString(inventory)))
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(inventory)));
	}

	@Test
	public void testDeleteInventorySuccessTest() throws Exception {
		Long inventarioId = 1L;
		
		when(inventoryService.eliminar(inventarioId)).thenReturn(true);
		mockMvc.perform(delete("/api/inventory/delete/{inventarioId}", inventarioId))
			.andExpect(status().isOk())
			.andExpect(content().string("Inventario borrado exitosamente"));
		
		verify(inventoryService, times(1)).eliminar(inventarioId);
	}

	@Test
	public void testDeleteInventoryNotFoundTest() throws Exception {
		Long categoriaId = 99L;
		
		when(inventoryService.eliminar(categoriaId)).thenReturn(false);
		mockMvc.perform(delete("/api/inventory/delete/{categoriaId}", categoriaId))
			.andExpect(status().isNotFound()); 
		
		verify(inventoryService, times(1)).eliminar(categoriaId);
	}
}
