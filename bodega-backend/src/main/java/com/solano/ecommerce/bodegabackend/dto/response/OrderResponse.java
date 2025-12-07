package com.solano.ecommerce.bodegabackend.dto.response;

import com.solano.ecommerce.bodegabackend.model.enums.DeliveryMethod;
import com.solano.ecommerce.bodegabackend.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String code;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private DeliveryMethod deliveryMethod;
    private BigDecimal totalAmount;
    private String paymentProofUrl;
    private List<OrderItemResponse> items;
}
