package com.letrasypapeles.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.letrasypapeles.backend.entity.Inventory;
import com.letrasypapeles.backend.repository.InventoryRepository;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

  @Mock
  private InventoryRepository inventoryRepository;

  @InjectMocks
  private InventoryService inventoryService;

  private Inventory inventory;

  @BeforeEach
  public void setUp(){
    inventory = Inventory.builder()
      .id(1L)
      .stock(100)
      .threshold(10)
      .build();
  }

  @Test
  public void testGetAllInventory(){
    List<Inventory> expected = List.of(inventory);
    when(inventoryRepository.findAll()).thenReturn(expected);
		assertEquals(expected, inventoryService.obtenerTodos());
  }

  @Test
  public void testGetInventoryById() {
    when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
    assertEquals(Optional.of(inventory), inventoryService.obtenerPorId(1L));
	}

  @Test
  public void testCreateInventory() {
    when(inventoryRepository.save(inventory)).thenReturn(inventory);
    assertEquals(inventory, inventoryService.guardar(inventory));
  }

  @Test
  public void testDeleteInventorySuccess() {
    when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
    boolean result = inventoryService.eliminar(1L);
    assertTrue(result);
    verify(inventoryRepository, times(1)).findById(1L);
    verify(inventoryRepository, times(1)).deleteById(1L);

  }

  @Test
  public void testDeleteInventoryFail() {
    Long id = inventory.getId();
    when(inventoryRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = inventoryService.eliminar(id);
        
    assertFalse(result);
    verify(inventoryRepository, times(1)).findById(id);
    verify(inventoryRepository, never()).deleteById(any());
  }
  
}
