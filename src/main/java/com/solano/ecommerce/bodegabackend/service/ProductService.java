package com.solano.ecommerce.bodegabackend.service;

import com.solano.ecommerce.bodegabackend.dto.request.ProductRequest;
import com.solano.ecommerce.bodegabackend.dto.response.ProductResponse;
import com.solano.ecommerce.bodegabackend.model.CategoryProduct;
import com.solano.ecommerce.bodegabackend.model.Product;
import com.solano.ecommerce.bodegabackend.repository.CategoryProductRepository;
import com.solano.ecommerce.bodegabackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;

    public Product createProduct(ProductRequest request) {
        Long siguienteNumero = productRepository.getNextCodigoSecuencial();

        String code = String.format("PROD%06d", siguienteNumero);

        CategoryProduct category = categoryProductRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new RuntimeException("Categoria con: " + request.getCategoryId() + " no encontrado")
        );

        Product product = Product.builder()
                .code(code)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(category)
                .active(true)
                .build();
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con: " + id + " no encontrado"));
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getAllProductsForAdmin() {
        return productRepository.findAll();
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con: " + id + " no encontrado"));

        CategoryProduct category = categoryProductRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new RuntimeException("Categoria con: " + request.getCategoryId() + " no encontrado")
        );

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setCategory(category);

        Product savedProduct = productRepository.save(existingProduct);

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .code(savedProduct.getCode())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .imageUrl(savedProduct.getImageUrl())
                .categoryId(savedProduct.getCategory().getId())
                .active(savedProduct.isActive() ? "Active" : "Inactive")
                .build();
    }

    // Soft delete
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con: " + id + " no encontrado"));
        existingProduct.setActive(false);
        productRepository.save(existingProduct);
    }
}
