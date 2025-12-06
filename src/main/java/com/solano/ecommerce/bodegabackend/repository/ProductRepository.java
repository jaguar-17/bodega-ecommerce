package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // Obtiene todos los productos activos
    List<Product> findByActiveTrue();

    // Busca productos por nombre (búsqueda parcial, sin distinguir mayúsculas/minúsculas)
    List<Product> findByNameContainingIgnoreCase(String name);
}
