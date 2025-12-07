package com.solano.ecommerce.bodegabackend.dto.request;

import com.solano.ecommerce.bodegabackend.model.enums.DeliveryMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<OrderItemRequest> items;
    private DeliveryMethod deliveryMethod;
}
