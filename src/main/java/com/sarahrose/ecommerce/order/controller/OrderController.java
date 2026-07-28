package com.sarahrose.ecommerce.order.controller;

import com.sarahrose.ecommerce.order.dto.CreateOrderRequest;
import com.sarahrose.ecommerce.order.dto.OrderResponse;
import com.sarahrose.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(
            @RequestBody @Valid CreateOrderRequest request) {

        return orderService.createOrder(request);
    }
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}