package com.letrasypapeles.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository; 

import com.letrasypapeles.backend.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  
}
