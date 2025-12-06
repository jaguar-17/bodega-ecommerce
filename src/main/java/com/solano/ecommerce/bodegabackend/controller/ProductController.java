package com.solano.ecommerce.bodegabackend.controller;

import com.solano.ecommerce.bodegabackend.dto.request.ProductRequest;
import com.solano.ecommerce.bodegabackend.dto.response.ProductResponse;
import com.solano.ecommerce.bodegabackend.model.Product;
import com.solano.ecommerce.bodegabackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // ====================== CLIENTE ======================
    // Endpoint: Obtener todos los productos activos
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    // Endpoint: Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ====================== ADMIN ======================
    // Endpoint: Obtener todos los productos (incluyendo inactivos)
    @GetMapping("/admin")
    public ResponseEntity<List<Product>> getAllProductsForAdmin() {
        return ResponseEntity.ok(productService.getAllProductsForAdmin());
    }

    // Endpoint: Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    // Endpoint: Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // Endpoint: Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
