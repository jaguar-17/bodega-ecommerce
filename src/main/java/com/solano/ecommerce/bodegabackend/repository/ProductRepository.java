package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(code, 5) AS long)), 0) + 1 FROM Product WHERE code LIKE 'PROD%'")
    Long getNextCodigoSecuencial();

    // Obtiene todos los productos activos
    List<Product> findByActiveTrue();

    // Busca productos por nombre (búsqueda parcial, sin distinguir mayúsculas/minúsculas)
    List<Product> findByNameContainingIgnoreCase(String name);
}
