package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    // Obtiene todas las órdenes de un usuario específico, ordenadas por fecha de creación descendente
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
