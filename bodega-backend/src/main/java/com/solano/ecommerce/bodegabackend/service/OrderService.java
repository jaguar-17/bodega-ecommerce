package com.solano.ecommerce.bodegabackend.service;

import com.solano.ecommerce.bodegabackend.dto.request.OrderItemRequest;
import com.solano.ecommerce.bodegabackend.dto.request.OrderRequest;
import com.solano.ecommerce.bodegabackend.dto.response.OrderItemResponse;
import com.solano.ecommerce.bodegabackend.dto.response.OrderResponse;
import com.solano.ecommerce.bodegabackend.model.Order;
import com.solano.ecommerce.bodegabackend.model.OrderItem;
import com.solano.ecommerce.bodegabackend.model.Product;
import com.solano.ecommerce.bodegabackend.model.User;
import com.solano.ecommerce.bodegabackend.model.enums.OrderStatus;
import com.solano.ecommerce.bodegabackend.repository.OrderRepository;
import com.solano.ecommerce.bodegabackend.repository.ProductRepository;
import com.solano.ecommerce.bodegabackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    // Configurar Pago
    public OrderResponse uploadPaymentProof(Long orderId, String userEmail, MultipartFile file) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden con ID: " + orderId + " no encontrada."));

        // Verificar que la orden pertenece al usuario
        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("No tienes permiso para subir el comprobante de pago de esta orden.");
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("Esta orden ya no está pendiente de pago.");
        }

        // Subir el comprobante a Cloudinary
        String imageUrl = cloudinaryService.upload(file, "comprobantes-pagos");

        // Actualizar la orden con el URL del comprobante y cambiar el estado
        order.setPaymentProofUrl(imageUrl);
        order.setStatus(OrderStatus.VERIFYING_PAYMENT);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    @Transactional
    public OrderResponse createOrder(String userEmail, OrderRequest request) {
        Long siguienteNumero = orderRepository.getNextCodigoSecuencial();

        String code = String.format("ORDR%06d", siguienteNumero);

        // 1. Obtenemos al usuario
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la orden vacia inicialmente
        Order order = Order.builder()
                .code(code)
                .user(user)
                .status(OrderStatus.PENDING_PAYMENT)
                .deliveryMethod(request.getDeliveryMethod())
                .build();

        // 3. Procesar los items y calcular total
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto con ID: " + itemRequest.getProductId() + " no encontrado."));

            if (!product.isActive()) {
                throw new RuntimeException("El producto " + product.getName() + " no está disponible.");
            }

            // Calcular subtotal
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(subtotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .subtotal(subtotal)
                    .build();

            items.add(orderItem);
        }

        // 4. Asignar items y total a la orden
        order.setItems(items);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // 5. Guardar la orden
        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario con email: " + userEmail + " no encontrado"));

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();

        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden con ID: " + orderId + " no encontrada."));

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    // --- MAPPERS  ---
    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .code(order.getCode())
                .user(order.getUser().getFullName())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .deliveryMethod(order.getDeliveryMethod())
                .totalAmount(order.getTotalAmount())
                .paymentProofUrl(order.getPaymentProofUrl())
                // Convertimos también la lista de items
                .items(order.getItems().stream().map(this::mapToItemResponse).collect(Collectors.toList()))
                .build();
    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productImageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
