package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(code, 5) AS long)), 0) + 1 FROM Order WHERE code LIKE 'ORDR%'")
    Long getNextCodigoSecuencial();

    // Obtiene todas las órdenes de un usuario específico, ordenadas por fecha de creación descendente
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
