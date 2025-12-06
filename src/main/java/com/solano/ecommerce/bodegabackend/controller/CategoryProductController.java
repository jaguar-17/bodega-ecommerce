package com.solano.ecommerce.bodegabackend.controller;

import com.solano.ecommerce.bodegabackend.dto.request.CategoryProductRequest;
import com.solano.ecommerce.bodegabackend.model.CategoryProduct;
import com.solano.ecommerce.bodegabackend.service.CategoryProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category-products")
@RequiredArgsConstructor
public class CategoryProductController {
    private final CategoryProductService categoryProductService;

    // Endpoint: Obtener todas las categorías de productos
    @GetMapping
    public ResponseEntity<List<CategoryProduct>> getAllCategoryProducts() {
        return ResponseEntity.ok(categoryProductService.getAllCategoryProducts());
    }

    // Endpoint: Crear una nueva categoría de producto
    @PostMapping
    public ResponseEntity<CategoryProduct> createCategoryProduct(@RequestBody CategoryProductRequest request) {
        return ResponseEntity.ok(categoryProductService.createCategoryProduct(request));
    }

    // Endpoint: Actualizar una categoría de producto existente
    @PutMapping("/{id}")
    public ResponseEntity<CategoryProduct> updateCategoryProduct(@PathVariable Long id, @RequestBody CategoryProductRequest request) {
        return ResponseEntity.ok(categoryProductService.updateCategoryProduct(id, request));
    }

    // Endpoint: Eliminar una categoría de producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryProduct(@PathVariable Long id) {
        categoryProductService.deleteCategoryProduct(id);
        return ResponseEntity.noContent().build();
    }
}
