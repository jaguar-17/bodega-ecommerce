package com.solano.ecommerce.bodegabackend.controller;

import com.solano.ecommerce.bodegabackend.dto.request.ProductRequest;
import com.solano.ecommerce.bodegabackend.dto.response.ProductResponse;
import com.solano.ecommerce.bodegabackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    // Endpoint: Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ====================== ADMIN ======================
    // Endpoint: Obtener todos los productos (incluyendo inactivos)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<ProductResponse>> getAllProductsForAdmin() {
        return ResponseEntity.ok(productService.getAllProductsForAdmin());
    }

    // Endpoint: Crear un nuevo producto
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    // Endpoint: Actualizar un producto existente
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // Endpoint: Actualizar el estado activo/inactivo de un producto
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) { // Recibe ?active=true o ?active=false

        return ResponseEntity.ok(productService.updateProductStatus(id, active));
    }

    // Endpoint: Eliminar un producto
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
