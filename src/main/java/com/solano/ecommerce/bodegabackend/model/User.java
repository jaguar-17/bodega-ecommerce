package com.solano.ecommerce.bodegabackend.model;

import com.solano.ecommerce.bodegabackend.model.enums.AuthProvider;
import com.solano.ecommerce.bodegabackend.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // Puede ser nulo si el usuario se registra con Google

    private String fullName;

    private String phone; // Para coordinación de entregas o yape

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
}
