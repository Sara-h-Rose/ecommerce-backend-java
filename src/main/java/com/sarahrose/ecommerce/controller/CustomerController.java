package com.sarahrose.ecommerce.controller;

import com.sarahrose.ecommerce.dto.CustomerRequest;
import com.sarahrose.ecommerce.dto.CustomerResponse;
import com.sarahrose.ecommerce.dto.OrderResponse;
import com.sarahrose.ecommerce.service.CustomerService;
import com.sarahrose.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
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

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable Long id,
            @RequestBody @Valid CustomerRequest request) {

        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
