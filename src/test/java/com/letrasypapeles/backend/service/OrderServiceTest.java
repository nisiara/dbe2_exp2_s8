package com.letrasypapeles.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.OrderRepository;
import com.letrasypapeles.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private UserRepository userRepository;  

  @InjectMocks
  private OrderService orderService;

  private Order orden;
  private User usuario;

  @BeforeEach
  public void setUp(){
    usuario = User.builder()
      .id(1L)
      .build();

    orden = Order.builder()
      .id(1L)
      .user(usuario)
      .status("CONFIRMADA")
      .build();
  }

  @Test
  public void testGetAllOrders(){
    List<Order> expected = List.of(orden);
    when(orderRepository.findAll()).thenReturn(expected);
		assertEquals(expected, orderService.obtenerTodos());
  }

  @Test
  public void testGetOrderById() {
    Long id = orden.getId();
    when(orderRepository.findById(id)).thenReturn(Optional.of(orden));
    assertEquals(Optional.of(orden), orderService.obtenerPorId(id));
	}

  @Test
  public void testGetOrderByUserId() {
    Long id = usuario.getId();
    List<Order> expectedOrders = List.of(orden);
    when(orderRepository.findByUserId(id)).thenReturn(expectedOrders);
    assertEquals(expectedOrders, orderService.obtenerPorUserId(id));
	}

  @Test
  public void testGetOrderByStatus() {
    String status = "CONFIRMADA";
    List<Order> expectedOrders = List.of(orden);
    when(orderRepository.findByStatus(status)).thenReturn(expectedOrders);
    assertEquals(expectedOrders, orderService.obtenerPorEstado(status));
	}

  @Test
  public void testCreateOrder() {
    when(orderRepository.save(orden)).thenReturn(orden);
    assertEquals(orden, orderService.guardar(orden));
  }

  @Test
  public void testUpdateOrderSuccess() {
    when(orderRepository.existsById(1L)).thenReturn(true);
    when(orderRepository.save(orden)).thenReturn(orden);
    Order result = orderService.actualizarOrden(1L, orden);
    assertEquals(1L, orden.getId());
    assertEquals(orden, result);
    verify(orderRepository).save(orden);
  }

  @Test
  public void testUpdateOrderFail() {
    when(orderRepository.existsById(1L)).thenReturn(false);
    assertNull(orderService.actualizarOrden(1L, orden));
    verify(orderRepository, never()).save(any());
  }


  @Test
  public void testDeleteOrderSuccess() {
    Long id = orden.getId();
    when(orderRepository.findById(id)).thenReturn(Optional.of(orden));
    boolean result = orderService.eliminar(id);
    assertTrue(result);
    verify(orderRepository, times(1)).findById(id);
    verify(orderRepository, times(1)).deleteById(id);

  }

  @Test
  public void testDeleteOrderFail() {
    Long id = orden.getId();
    when(orderRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = orderService.eliminar(id);
        
    assertFalse(result);
    verify(orderRepository, times(1)).findById(id);
    verify(orderRepository, never()).deleteById(any());
  }

}
