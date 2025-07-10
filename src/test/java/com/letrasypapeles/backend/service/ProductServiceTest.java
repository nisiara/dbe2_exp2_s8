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

import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;  

  private Product producto;

  @BeforeEach
  public void setUp(){
    producto = Product.builder()
      .id(1L)
      .name("Producto")
      .stock(1)
      .build();
  }

  @Test
  public void testGetAllProducts(){
    List<Product> expected = List.of(producto);
    when(productRepository.findAll()).thenReturn(expected);
		assertEquals(expected, productService.obtenerTodos());
  }

  @Test
  public void testGetProductById() {
    Long id = producto.getId();
    when(productRepository.findById(id)).thenReturn(Optional.of(producto));
    assertEquals(Optional.of(producto), productService.obtenerPorId(id));
	}

  @Test
  public void testCreateProduct() {
    when(productRepository.save(producto)).thenReturn(producto);
    assertEquals(producto, productService.guardar(producto));
  }

  @Test
  public void testUpdateProductSuccess() {
    when(productRepository.existsById(1L)).thenReturn(true);
    when(productRepository.save(producto)).thenReturn(producto);
    Product result = productService.actualizar(1L, producto);
    assertEquals(1L, producto.getId());
    assertEquals(producto, result);
    verify(productRepository).save(producto);
  }

  @Test
  public void testUpdateBranchNotFound() {
    when(productRepository.existsById(1L)).thenReturn(false);
    assertNull(productService.actualizar(1L, producto));
    verify(productRepository, never()).save(any());
  }


  @Test
  public void testDeleteProductSuccess() {
    Long id = producto.getId();
    when(productRepository.findById(id)).thenReturn(Optional.of(producto));
    boolean result = productService.eliminar(id);
    assertTrue(result);
    verify(productRepository, times(1)).findById(id);
    verify(productRepository, times(1)).deleteById(id);

  }

  @Test
  public void testDeleteProductFail() {
    Long id = producto.getId();
    when(productRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = productService.eliminar(id);
        
    assertFalse(result);
    verify(productRepository, times(1)).findById(id);
    verify(productRepository, never()).deleteById(any());
  }
}
