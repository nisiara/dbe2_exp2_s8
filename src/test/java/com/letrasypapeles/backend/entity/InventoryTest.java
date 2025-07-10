package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryTest {
	@Test
	public void testGettersAndSetters() {
		Inventory inventory = new Inventory();
		inventory.setId(1L);
		inventory.setStock(10);
		inventory.setThreshold(2);
		Assertions.assertEquals(1L, inventory.getId());

	}
}
