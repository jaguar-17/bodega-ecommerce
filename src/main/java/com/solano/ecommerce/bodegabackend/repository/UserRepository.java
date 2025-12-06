package com.solano.ecommerce.bodegabackend.repository;

import com.solano.ecommerce.bodegabackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Obtiene un usuario por su email
    Optional<User> findByEmail(String email);

    // Verifica si un usuario existe por su email
    boolean existsByEmail(String email);
}
