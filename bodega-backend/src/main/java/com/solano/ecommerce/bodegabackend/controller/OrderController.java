package com.solano.ecommerce.bodegabackend.controller;

import com.solano.ecommerce.bodegabackend.dto.request.OrderRequest;
import com.solano.ecommerce.bodegabackend.dto.response.OrderResponse;
import com.solano.ecommerce.bodegabackend.model.enums.OrderStatus;
import com.solano.ecommerce.bodegabackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Endpoint: Crear una nueva orden
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(orderService.createOrder(email, request));
    }

    // Endpoint: Confirmar pago subiendo el comprobante
    @PostMapping("/{id}/confirm-payment")
    public ResponseEntity<OrderResponse> confirmPayment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(orderService.uploadPaymentProof(id, email, file));
    }

    // Endpoint: Obtener las órdenes del usuario autenticado
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(orderService.getUserOrders(email));
    }

    // ====================== ADMIN ======================
    // Endpoint: Obtener todas las órdenes
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Endpoint: Actualizar el estado de una orden
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
