package com.solano.ecommerce.bodegabackend.controller;

import com.solano.ecommerce.bodegabackend.dto.request.OrderRequest;
import com.solano.ecommerce.bodegabackend.dto.response.OrderResponse;
import com.solano.ecommerce.bodegabackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    // Endpoint: Obtener las órdenes del usuario autenticado
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(orderService.getUserOrders(email));
    }
}
