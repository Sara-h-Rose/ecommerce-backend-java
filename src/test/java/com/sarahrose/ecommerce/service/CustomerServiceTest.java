package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.CustomerRequest;
import com.sarahrose.ecommerce.dto.CustomerResponse;
import com.sarahrose.ecommerce.exception.AccessDeniedException;
import com.sarahrose.ecommerce.exception.EmailAlreadyExistsException;
import com.sarahrose.ecommerce.exception.ResourceNotFoundException;
import com.sarahrose.ecommerce.model.Customer;
import com.sarahrose.ecommerce.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setEmail("john@example.com");
        customer.setActive(true);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "john@example.com",
                        null,
                        java.util.Collections.emptyList()
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        CustomerResponse response =
                customerService.getCustomerById(1L);

        assertEquals(1L, response.id());
        assertEquals("John", response.name());
        assertEquals("john@example.com", response.email());
        assertTrue(response.active());

        verify(customerRepository).findById(1L);
    }

    @Test
    void deleteCustomer_shouldSoftDeleteCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        assertFalse(customer.isActive());

        verify(customerRepository).save(customer);
    }

    @Test
    void getCustomerById_shouldThrowExceptionForInactiveCustomer() {

        customer.setActive(false);

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.getCustomerById(1L)
        );

        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_shouldThrowExceptionWhenAccessingAnotherCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // Simulate John being logged in
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "john@example.com",
                        null,
                        java.util.Collections.emptyList()
                )
        );

        // Customer 1 belongs to Sarah
        customer.setEmail("sarah.updated@example.com");

        assertThrows(
                AccessDeniedException.class,
                () -> customerService.getCustomerById(1L)
        );

        verify(customerRepository).findById(1L);
    }

    @Test
    void updateCustomer_shouldUpdateCustomerSuccessfully() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(customerRepository.existsByEmail("john.updated@example.com"))
                .thenReturn(false);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        CustomerRequest request = new CustomerRequest();
        request.setName("John Updated");
        request.setEmail("john.updated@example.com");

        CustomerResponse response =
                customerService.updateCustomer(1L, request);

        assertEquals("John Updated", response.name());
        assertEquals("john.updated@example.com", response.email());

        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_shouldThrowExceptionForDuplicateEmail() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(customerRepository.existsByEmail("sarah.updated@example.com"))
                .thenReturn(true);

        CustomerRequest request = new CustomerRequest();
        request.setName("John");
        request.setEmail("sarah.updated@example.com");

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> customerService.updateCustomer(1L, request)
        );

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_shouldThrowExceptionWhenAccessingAnotherCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        customer.setEmail("sarah.updated@example.com");

        CustomerRequest request = new CustomerRequest();
        request.setName("Sarah Hacked");
        request.setEmail("sarah.updated@example.com");

        assertThrows(
                AccessDeniedException.class,
                () -> customerService.updateCustomer(1L, request)
        );

        verify(customerRepository, never()).save(any(Customer.class));
    }


}