package com.letrasypapeles.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.service.OrderService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest(
	controllers = OrderController.class,
	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
)
public class OrderControllerTest {
  @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	private Order order;
  private User user;

  
  @BeforeEach
	public void setUp() {

    user = new User();
    user.setId(1L); 

		order = new Order();
		order.setId(1L);
    order.setStatus("Confirmada");
    order.setUser(user);
	}

  @Test
	public void testGetOrderList() throws Exception {
		when(orderService.obtenerTodos()).thenReturn(Collections.singletonList(order));
		mockMvc.perform(get("/api/order"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(order))));
	}

  @Test
	public void testGetOrderById() throws Exception {
		when(orderService.obtenerPorId(1L)).thenReturn(Optional.of(order));
		mockMvc.perform(get("/api/order/1"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(order)));
	}

  @Test
	public void testCreateOrder() throws Exception {
		when(orderService.guardar(order)).thenReturn(order);
		mockMvc.perform(post("/api/order/create")
				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
				.content(objectMapper.writeValueAsString(order)))
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(order)));
	}

  @Test
	public void testUpdateOrder() throws Exception {
		when(orderService.actualizarOrden(eq(1L), any(Order.class))).thenReturn(order);
		mockMvc.perform(put("/api/order/update/1")
			.contentType(String.valueOf(MediaType.APPLICATION_JSON))
			.content(objectMapper.writeValueAsString(order)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(order)));
	}

  @Test
  public void testGetOrderByUser() throws Exception {
		Long userId = 1L;
		List<Order> expectedOrders = Arrays.asList(order);

		when(orderService.obtenerPorUserId(userId)).thenReturn(expectedOrders);

		mockMvc.perform(get("/api/order/byUser/{userId}", userId)
			.accept(String.valueOf(MediaType.APPLICATION_JSON)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(expectedOrders)));
	}

  @Test
	public void testDeleteOrderSuccessTest() throws Exception {
		Long ordenId = 1L;
		
		when(orderService.eliminar(ordenId)).thenReturn(true);
		mockMvc.perform(delete("/api/order/delete/{ordenId}", ordenId))
			.andExpect(status().isOk())
			.andExpect(content().string("Orden borrada exitosamente"));
		
		verify(orderService, times(1)).eliminar(ordenId);
	}

	@Test
	public void testDeleteOrderNotFoundTest() throws Exception {
		Long orderId = 99L;
		
		when(orderService.eliminar(orderId)).thenReturn(false);
		mockMvc.perform(delete("/api/order/delete/{orderId}", orderId))
			.andExpect(status().isNotFound()); 
		
		verify(orderService, times(1)).eliminar(orderId);
	}
  
}
