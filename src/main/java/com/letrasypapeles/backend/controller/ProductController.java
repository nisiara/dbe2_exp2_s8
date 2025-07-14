package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.ProductDTO;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Producto", description = "Operaciones relacionadas con los productos")
public class ProductController {

	private ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	/* 
	 *
	 * OBTENER LA LISTA DE PRODUCTOS
	 * 
	*/
	@Operation(
		summary = "Obtener la lista con los productos",
		description = "Este endpoint retorna la lista de productos disponibles en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de productos obtenida exitosamente",
				
				content = @Content(
          mediaType = "application/json",
					// Swagger s mostrando Product, pero la respuesta real incluirá enlaces
          array = @ArraySchema(schema = @Schema(implementation = Product.class)) 
        )
			),
			@ApiResponse(
					responseCode = "204",
					description = "No hay productos disponibles (La lista está vacía)"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor"
			)
		}
	)
	@GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Product>>> obtenerTodos() {
    List<Product> products = productService.obtenerTodos();

    if (products.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Convertir cada Product a EntityModel<Product> y añadir enlaces
    List<EntityModel<Product>> productModels = products.stream()
			.map(product -> {
					// Crear el enlace a sí mismo (self link)
					Link selfLink = linkTo(methodOn(ProductController.class).obtenerPorId(product.getId())).withSelfRel();
					// Crear enlaces adicionales para otras operaciones
					Link allProductsLink = linkTo(methodOn(ProductController.class).obtenerTodos()).withRel("allProducts");
					Link createProductLink = linkTo(methodOn(ProductController.class).crearProducto(new ProductDTO())).withRel("createProduct");
					Link updateProductLink = linkTo(methodOn(ProductController.class).actualizarProducto(product.getId(), new ProductDTO())).withRel("updateProduct");
					Link deleteProductLink = linkTo(methodOn(ProductController.class).delete(product.getId())).withRel("deleteProduct");

					return EntityModel.of(product, selfLink, allProductsLink, createProductLink, updateProductLink, deleteProductLink);
			})
			.collect(Collectors.toList());

    // Crear un enlace self para la colección completa
    Link collectionLink = linkTo(methodOn(ProductController.class).obtenerTodos()).withSelfRel();

    return new ResponseEntity<>(CollectionModel.of(productModels, collectionLink), HttpStatus.OK);
  }


	/* 
	 *
	 * OBTENER UN PRODUCTO POR ID 
	 * 
	*/
	@Operation(
		summary = "Obtiene un producto por ID",
		description = "Recupera los detalles de un producto específico utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Producto encontrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Product.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Producto no encontrado con el ID proporcionado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "ID de producto inválido"
			)
		}
	)
	@GetMapping("/{id}")
  public ResponseEntity<EntityModel<Product>> obtenerPorId(
	@Parameter(
		name = "id",
		description = "Identificador único del producto. Debe ser un entero positivo,",
		example = "123",
		required = true
	)
	@PathVariable Long id) {
	return productService.obtenerPorId(id)
		.map(product -> {
			Link selfLink = linkTo(methodOn(ProductController.class).obtenerPorId(product.getId())).withSelfRel();
			Link allProductsLink = linkTo(methodOn(ProductController.class).obtenerTodos()).withRel("allProducts");
			Link createProductLink = linkTo(methodOn(ProductController.class).crearProducto(new ProductDTO())).withRel("createProduct");
			Link updateProductLink = linkTo(methodOn(ProductController.class).actualizarProducto(product.getId(), new ProductDTO())).withRel("updateProduct");
			Link deleteProductLink = linkTo(methodOn(ProductController.class).delete(product.getId())).withRel("deleteProduct");

			return ResponseEntity.ok(EntityModel.of(product, selfLink, allProductsLink, createProductLink, updateProductLink, deleteProductLink));
		})
		.orElse(ResponseEntity.notFound().build());
	}

	/* 
	 *
	 * CREAR UN NUEVO PRODUCTO
	 * 
	*/
	@Operation(
		summary = "Crear un producto",
    description = "Crea un nuevo producto ingresando los datos requeridos.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Detalles del producto a crear",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ProductDTO.class),
				examples = {
					@ExampleObject(
						name = "Producto completo",
						summary = "Ejemplo de producto con todos los atributos",
						value = """
						{
							"name": "Lorem ipsum dolor sit amet",
							"details": "Nulla facilisis bibendum est eu aliquam. Morbi tincidunt commodo interdum. Sed congue risus a tortor.",
							"price": 1200,
							"stock": 50
						}
						"""
					)
				}
			)
    ),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Producto creado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Product.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Datos del producto inválidos o incompletos"
			),
			@ApiResponse(
				responseCode = "409",
				description = "Conflicto: un producto con datos similares ya existe"
			)
		}
		
	)
	
	@PostMapping
	public ResponseEntity<EntityModel<Product>> crearProducto(@RequestBody ProductDTO productDTO) {
		
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setDetails(productDTO.getDetails());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());  

		System.out.println("Product a guardar: " + product);
		Product savedProduct = null;
			
		try {
      savedProduct = productService.guardar(product);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (savedProduct == null) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
			
    Link selfLink = linkTo(methodOn(ProductController.class).obtenerPorId(savedProduct.getId())).withSelfRel();
    Link allProductsLink = linkTo(methodOn(ProductController.class).obtenerTodos()).withRel("allProducts");
    Link createProductLink = linkTo(methodOn(ProductController.class).crearProducto(new ProductDTO())).withRel("createProduct");
    Link updateProductLink = linkTo(methodOn(ProductController.class).actualizarProducto(savedProduct.getId(), new ProductDTO())).withRel("updateProduct");
    Link deleteProductLink = linkTo(methodOn(ProductController.class).delete(savedProduct.getId())).withRel("deleteProduct");

    return new ResponseEntity<>(EntityModel.of(savedProduct, selfLink, allProductsLink, createProductLink, updateProductLink, deleteProductLink), HttpStatus.CREATED);
  }


	/* 
	 *
	 * ACTUALIZAR EL PRODUCTO
	 * 
	*/
	@Operation(
		summary = "Actualiza un producto existente",
		description = "Actualiza completamente un producto existente utilizando su ID",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos actualizados del producto",
      required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Product.class),
				examples = @ExampleObject(
					name = "Producto actualizado",
					value = """
						{
							"name": "Producto actualizado",
							"details": "Agregamos los detalles actualizados del producto.",
							"price": 1350.00,
							"stock": 30
						}
						"""
				)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Producto actualizado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Product.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Producto no encontrado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "Datos de producto inválidos"
			)
		}
	)

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Product>> actualizarProducto(
		@Parameter(
			description = "ID del producto a actualizar", 
			example = "123", 
			required = true
		)
		@PathVariable Long id, @RequestBody ProductDTO productDTO) {

		Product product = new Product();
		product.setName(productDTO.getName());
		product.setDetails(productDTO.getDetails());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());  
			
		Product updatedProduct = productService.actualizar(id, product);
		if (updatedProduct == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
    Link selfLink = linkTo(methodOn(ProductController.class).obtenerPorId(updatedProduct.getId())).withSelfRel();
    Link allProductsLink = linkTo(methodOn(ProductController.class).obtenerTodos()).withRel("allProducts");
    Link createProductLink = linkTo(methodOn(ProductController.class).crearProducto(new ProductDTO())).withRel("createProduct");
    Link updateProductLink = linkTo(methodOn(ProductController.class).actualizarProducto(updatedProduct.getId(), new ProductDTO())).withRel("updateProduct");
    Link deleteProductLink = linkTo(methodOn(ProductController.class).delete(updatedProduct.getId())).withRel("deleteProduct");

    return ResponseEntity.ok(EntityModel.of(updatedProduct, selfLink, allProductsLink, createProductLink, updateProductLink, deleteProductLink));
	}

	/* 
	 *
	 * ELIMAR UN PRODUCTO
	 * 
	*/
	@Operation(
		summary = "Elimina un producto",
		description = "Elimina un producto específico del sistema utilizando su ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Producto eliminado exitosamente",
				content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
						value = """
							{
								"message": "Producto eliminado exitosamente"
							}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Producto no encontrado"
			)
		}
	)
	
	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> delete(
		@Parameter(
			description = "ID del producto a eliminar", 
			example = "456", 
			required = true
		)	
		@PathVariable Long id) {
		
			boolean isDeleted = productService.eliminar(id);
			if(isDeleted){
				Map<String, String> response = new HashMap<>();
				response.put("message", "Producto eliminado exitosamente");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} 
			else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}


}
