package com.letrasypapeles.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name="tbl_products")
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("name")
	private String name;
	@JsonProperty("details")
	private String details;
	@JsonProperty("price")
	private double price;
	@JsonProperty("stock")
	private int stock;



}

	// @ManyToOne
	// @JoinColumn(name="category_id")
	// @JsonProperty("category")
	// private Category category;

	// @ManyToOne
	// @JoinColumn(name="supplier_id")
	// @JsonProperty("supplier")
	// private Supplier supplier;