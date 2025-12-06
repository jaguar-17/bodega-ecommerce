package com.solano.ecommerce.bodegabackend.model.enums;

public enum OrderStatus {
    PENDING_PAYMENT,    // Cliente creó pedido, falta subir foto Yape
    VERIFYING_PAYMENT,  // Cliente subió foto, Admin debe revisar
    IN_PREPARATION,     // Pago aprobado, bodeguero empacando
    ON_WAY,             // (Solo Delivery) Motorizado en camino
    READY_FOR_PICKUP,   // (Solo Recojo) Ven a buscarlo
    DELIVERED,          // Entregado
    CANCELLED           // Rechazado o cancelado
}
