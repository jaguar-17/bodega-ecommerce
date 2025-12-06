package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct,Long> {
}
