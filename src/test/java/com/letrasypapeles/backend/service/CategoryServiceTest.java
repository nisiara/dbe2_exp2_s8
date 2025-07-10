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

import com.letrasypapeles.backend.entity.Category;
import com.letrasypapeles.backend.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
  
  @Mock
  private CategoryRepository categoryRepository;;

  @InjectMocks
  private CategoryService categoryService;

  private Category categoria;

  @BeforeEach
  public void setUp(){
    categoria = new Category();
    categoria.setId(1L);
    categoria.setName("Papeleria");
  }

  @Test
  public void testGetCategoriesRoles(){
    List<Category> expected = List.of(categoria);
    when(categoryRepository.findAll()).thenReturn(expected);
		assertEquals(expected, categoryService.obtenerTodas());
  }

  @Test
  public void testGetCategoryByID() {
    Long id = categoria.getId();
    when(categoryRepository.findById(id)).thenReturn(Optional.of(categoria));
    assertEquals(Optional.of(categoria), categoryService.obtenerPorId(id));
	}

  @Test
  public void testCreateCategory() {
    when(categoryRepository.save(categoria)).thenReturn(categoria);
    assertEquals(categoria, categoryService.guardar(categoria));
  }

  @Test
  public void testDeleteCategorySuccess() {
    Long id = categoria.getId();
    when(categoryRepository.findById(id)).thenReturn(Optional.of(categoria));
    boolean result = categoryService.eliminar(id);
    assertTrue(result);
    verify(categoryRepository, times(1)).findById(id);
    verify(categoryRepository, times(1)).deleteById(id);
  }

  @Test
  public void testDeleteCategoryFail() {
    Long id = categoria.getId();
    when(categoryRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = categoryService.eliminar(id);
        
    assertFalse(result);
    verify(categoryRepository, times(1)).findById(id);
    verify(categoryRepository, never()).deleteById(any());
  }
}
