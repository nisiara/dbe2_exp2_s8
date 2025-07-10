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

import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {
  @Mock
  private SupplierRepository supplierRepository;

  @InjectMocks
  private SupplierService supplierService;

  private Supplier proveedor;

  @BeforeEach
  public void setUp(){
    proveedor = new Supplier();
    proveedor.setId(1L);
    proveedor.setName("El proveedor");
    proveedor.setContact("Contacto");
      
  }

  @Test
  public void testGetAllSuppliers(){
    List<Supplier> expected = List.of(proveedor);
    when(supplierRepository.findAll()).thenReturn(expected);
		assertEquals(expected, supplierService.obtenerTodos());
  }

  @Test
  public void testGetSupplierById() {
    when(supplierRepository.findById(1L)).thenReturn(Optional.of(proveedor));
    assertEquals(Optional.of(proveedor), supplierService.obtenerPorId(1L));
	}

  @Test
  public void testCreateSupplier() {
    when(supplierRepository.save(proveedor)).thenReturn(proveedor);
    assertEquals(proveedor, supplierService.guardar(proveedor));
  }

  @Test
  public void testDeleteSupplierSuccess() {
    when(supplierRepository.findById(1L)).thenReturn(Optional.of(proveedor));
    boolean result = supplierService.eliminar(1L);
    assertTrue(result);
    verify(supplierRepository, times(1)).findById(1L);
    verify(supplierRepository, times(1)).deleteById(1L);

  }

  @Test
  public void testDeleteSupplierFail() {
    Long id = proveedor.getId();
    when(supplierRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = supplierService.eliminar(id);
        
    assertFalse(result);
    verify(supplierRepository, times(1)).findById(id);
    verify(supplierRepository, never()).deleteById(any());
  }
  
}
