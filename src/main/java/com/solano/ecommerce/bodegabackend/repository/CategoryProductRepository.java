package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct,Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(code, 5) AS long)), 0) + 1 FROM CategoryProduct WHERE code LIKE 'CATP%'")
    Long getNextCodigoSecuencial();
}
