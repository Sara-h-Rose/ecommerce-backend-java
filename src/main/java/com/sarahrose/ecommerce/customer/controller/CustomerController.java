package com.sarahrose.ecommerce.customer.controller;

import com.sarahrose.ecommerce.customer.dto.CreateCustomerRequest;
import com.sarahrose.ecommerce.customer.dto.CustomerResponse;
import com.sarahrose.ecommerce.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.sarahrose.ecommerce.order.dto.OrderResponse;
import com.sarahrose.ecommerce.order.service.OrderService;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;
    public CustomerController(
            CustomerService customerService,
            OrderService orderService) {

        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping
    public CustomerResponse addCustomer(
            @RequestBody @Valid CreateCustomerRequest request) {
        return customerService.addCustomer(request);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/{id}/orders")
    public List<OrderResponse> getCustomerOrders(@PathVariable Long id) {
        return orderService.getOrdersByCustomer(id);
    }
}