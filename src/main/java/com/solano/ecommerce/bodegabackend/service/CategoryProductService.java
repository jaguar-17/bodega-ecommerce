package com.solano.ecommerce.bodegabackend.service;

import com.solano.ecommerce.bodegabackend.dto.request.CategoryProductRequest;
import com.solano.ecommerce.bodegabackend.model.CategoryProduct;
import com.solano.ecommerce.bodegabackend.repository.CategoryProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryProductService {
    private final CategoryProductRepository categoryProductRepository;

    public CategoryProduct createCategoryProduct(CategoryProductRequest request) {
        Long siguienteNumero = categoryProductRepository.getNextCodigoSecuencial();

        String code = String.format("CATP%06d", siguienteNumero);

        CategoryProduct categoryProduct = CategoryProduct.builder()
                .code(code)
                .name(request.getName())
                .build();
        return categoryProductRepository.save(categoryProduct);
    }

    public List<CategoryProduct> getAllCategoryProducts() {
        return categoryProductRepository.findAll();
    }

    public CategoryProduct updateCategoryProduct(Long id, CategoryProductRequest request) {
        CategoryProduct existingCategoryProduct = categoryProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria con: " + id + " no encontrado"));
        existingCategoryProduct.setName(request.getName());
        return categoryProductRepository.save(existingCategoryProduct);
    }

    public void deleteCategoryProduct(Long id) {
        CategoryProduct existingCategoryProduct = categoryProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria con: " + id + " no encontrado"));
        categoryProductRepository.delete(existingCategoryProduct);
    }
}
