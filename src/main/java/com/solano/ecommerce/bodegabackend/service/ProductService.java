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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;

    public ProductResponse createProduct(ProductRequest request) {
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

        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con: " + id + " no encontrado"));
        return mapToProductResponse(product);
    }

    public List<ProductResponse> getAllActiveProducts() {
        List<Product> products = productRepository.findByActiveTrue();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getAllProductsForAdmin() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
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

        return mapToProductResponse(savedProduct);
    }

    public ProductResponse updateProductStatus(Long id, boolean isActive) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con: " + id + " no encontrado"));

        existingProduct.setActive(isActive);
        Product savedProduct = productRepository.save(existingProduct);

        return mapToProductResponse(savedProduct);
    }

    // Soft delete
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con: " + id + " no encontrado"));
        existingProduct.setActive(false);
        productRepository.save(existingProduct);
    }

    // --- MÉTODO HELPER PRIVADO (La clave para evitar repetir código) ---
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                // Aquí es donde evitamos el error del Proxy al sacar solo el ID
                .categoryId(product.getCategory().getId())
                .active(product.isActive() ? "Active" : "Inactive")
                .build();
    }
}
