package com.qtrade.controller;

import com.qtrade.dto.OrderResponse;
import com.qtrade.dto.PlaceOrderRequest;
import com.qtrade.entity.Order;
import com.qtrade.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        Order order = orderService.placeOrder(request.getAccountId(), request.getStockId(),
                request.getQuantity(), request.getType());
        OrderResponse response = mapToOrderResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(mapToOrderResponse(order));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByAccount(@PathVariable Long accountId) {
        List<OrderResponse> orders = orderService.getOrdersByAccountId(accountId).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getType(),
                order.getQuantity(),
                order.getExecutionPrice(),
                order.getTimestamp(),
                order.getAccount().getId(),
                order.getStock().getTicker()
        );
    }
}