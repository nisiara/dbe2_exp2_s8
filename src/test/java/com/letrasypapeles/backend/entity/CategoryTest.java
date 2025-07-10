package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CategoryTest {
	@Test
	public void testGettersAndSetters() {
		Category category = new Category();
		category.setId(1L);
		category.setName("Novela");

		Assertions.assertEquals(1L, category.getId());
	}
}
