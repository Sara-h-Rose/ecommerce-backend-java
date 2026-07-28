package com.sarahrose.ecommerce.customer.controller;

import com.sarahrose.ecommerce.customer.dto.CreateCustomerRequest;
import com.sarahrose.ecommerce.customer.dto.CustomerResponse;
import com.sarahrose.ecommerce.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
}