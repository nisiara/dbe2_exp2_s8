package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.service.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Tag(name = "Órdenes", description = "Operaciones relacionadas con las Órdenes")
public class OrderController {

	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<List<Order>> obtenerTodos() {
		List<Order> orders = orderService.obtenerTodos();
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> obtenerPorId(@PathVariable Long id) {
		return orderService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/byUser/{userId}")
	public ResponseEntity<List<Order>> obtenerPorUserId(@PathVariable Long userId) {
		List<Order> ordersByUser = orderService.obtenerPorUserId(userId);
		return ResponseEntity.ok(ordersByUser);
	}

	@PostMapping("/create")
	public ResponseEntity<Order> crearPedido(@RequestBody Order order) {
		Order newOrder = orderService.guardar(order);
		return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Order> actualizarOrden(@PathVariable Long id, @RequestBody Order orden) {
		return ResponseEntity.ok(orderService.actualizarOrden(id, orden));
	}


	@DeleteMapping("/delete/{orderId}")
	ResponseEntity<String> delete(@PathVariable Long orderId) {
		boolean isDeleted = orderService.eliminar(orderId);
		if(isDeleted){
			return new ResponseEntity<>("Orden borrada exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}
