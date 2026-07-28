package com.sarahrose.ecommerce.customer.service;

import com.sarahrose.ecommerce.customer.dto.CreateCustomerRequest;
import com.sarahrose.ecommerce.customer.dto.CustomerResponse;
import com.sarahrose.ecommerce.customer.model.Customer;
import com.sarahrose.ecommerce.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.sarahrose.ecommerce.customer.exception.CustomerNotFoundException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse addCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        Customer savedCustomer = customerRepository.save(customer);

        return toCustomerResponse(savedCustomer);
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }

    public List<CustomerResponse> getAllCustomers(){
        return customerRepository.findAll()
                .stream()
                .map(this::toCustomerResponse)
                .toList();
    }
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = findCustomerById(id);
        return toCustomerResponse(customer);
    }
    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}