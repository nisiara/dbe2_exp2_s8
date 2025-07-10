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
import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.service.SupplierService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	controllers = SupplierController.class,
	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
)
public class SupplierControllerTest {
  @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SupplierService supplierService;

	@Autowired
	private ObjectMapper objectMapper;

	private Supplier proveedor;

  @BeforeEach
	public void setUp() {
		proveedor = new Supplier();
		proveedor.setId(1L);
    proveedor.setName("Proveedor test");

	}

  @Test
	public void testGetSupplierList() throws Exception {
		when(supplierService.obtenerTodos()).thenReturn(Collections.singletonList(proveedor));
		mockMvc.perform(get("/api/supplier"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(proveedor))));
	}

  @Test
	public void testGetSupplierById() throws Exception {
		when(supplierService.obtenerPorId(1L)).thenReturn(Optional.of(proveedor));
		mockMvc.perform(get("/api/supplier/1"))
		.andExpect(status().isOk())
		.andExpect(content().json(objectMapper.writeValueAsString(proveedor)));
	}
  
  @Test
	public void testCreateSupplier() throws Exception {
		when(supplierService.guardar(proveedor)).thenReturn(proveedor);
		mockMvc.perform(post("/api/supplier/create")
				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
				.content(objectMapper.writeValueAsString(proveedor)))
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(proveedor)));
	}

  @Test
	public void testDeleteSupplierSuccessTest() throws Exception {
		Long supplierId = 1L;
		
		when(supplierService.eliminar(supplierId)).thenReturn(true);
		mockMvc.perform(delete("/api/supplier/delete/{supplierId}", supplierId))
			.andExpect(status().isOk())
			.andExpect(content().string("Proveedor eliminado exitosamente"));
		
		verify(supplierService, times(1)).eliminar(supplierId);
	}

	@Test
	public void testDeletepRoleNotFoundTest() throws Exception {
		Long supplierId = 99L;
		
		when(supplierService.eliminar(supplierId)).thenReturn(false);
		mockMvc.perform(delete("/api/supplier/delete/{supplierId}", supplierId))
			.andExpect(status().isNotFound()); 
		
		verify(supplierService, times(1)).eliminar(supplierId);
	}
  
}
