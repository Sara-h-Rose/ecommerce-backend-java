package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.CustomerRequest;
import com.sarahrose.ecommerce.dto.CustomerResponse;
import com.sarahrose.ecommerce.exception.EmailAlreadyExistsException;
import com.sarahrose.ecommerce.exception.ResourceNotFoundException;
import com.sarahrose.ecommerce.model.Customer;
import com.sarahrose.ecommerce.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import com.sarahrose.ecommerce.security.SecurityUtil;
import com.sarahrose.ecommerce.exception.AccessDeniedException;
import com.sarahrose.ecommerce.security.SecurityUtil;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findByActiveTrue().stream()
                .map(this::toCustomerResponse)
                .toList();
    }

    public CustomerResponse getCustomerById(Long id) {

        Customer customer = getCustomer(id);

        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        if (!customer.getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException();
        }

        return toCustomerResponse(customer);
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {

        Customer customer = getCustomer(id);
        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        if (!customer.getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException();
        }

        if (!customer.getEmail().equals(request.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        return toCustomerResponse(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {

        Customer customer = getCustomer(id);

        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        if (!customer.getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException();
        }

        customer.setActive(false);

        customerRepository.save(customer);
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.isActive()
        );
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .filter(Customer::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer", 0L));
    }

    private Customer getAuthenticatedCustomer() {

        String email = SecurityUtil.getCurrentUserEmail();

        return customerRepository.findByEmail(email)
                .filter(Customer::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer", 0L));
    }
}
