package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.LoginRequest;
import com.sarahrose.ecommerce.dto.LoginResponse;
import com.sarahrose.ecommerce.dto.RegisterRequest;
import com.sarahrose.ecommerce.exception.EmailAlreadyExistsException;
import com.sarahrose.ecommerce.exception.InvalidCredentialsException;
import com.sarahrose.ecommerce.model.Customer;
import com.sarahrose.ecommerce.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(4L);
        customer.setName("John");
        customer.setEmail("john@example.com");
        customer.setPassword("$2a$10$hashedpassword");
        customer.setActive(true);
    }

    @Test
    void login_shouldReturnTokenForValidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("Password123");

        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(customer));

        when(passwordEncoder.matches(
                "Password123",
                "$2a$10$hashedpassword"))
                .thenReturn(true);

        when(jwtService.generateToken("john@example.com"))
                .thenReturn("test-jwt-token");

        LoginResponse response = authService.login(request);

        assertEquals("test-jwt-token", response.token());

        verify(customerRepository).findByEmail("john@example.com");
        verify(passwordEncoder).matches(
                "Password123",
                "$2a$10$hashedpassword");
        verify(jwtService).generateToken("john@example.com");
    }

    @Test
    void login_shouldThrowExceptionForUnknownEmail() {

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@example.com");
        request.setPassword("Password123");

        when(customerRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(anyString());
    }

    @Test
    void login_shouldThrowExceptionForWrongPassword() {

        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("WrongPassword");

        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(customer));

        when(passwordEncoder.matches(
                "WrongPassword",
                "$2a$10$hashedpassword"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never())
                .generateToken(anyString());
    }

    @Test
    void login_shouldThrowExceptionForInactiveCustomer() {

        customer.setActive(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("Password123");

        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(customer));

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(anyString());
    }

    @Test
    void register_shouldCreateCustomerWithEncodedPassword() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("Password123");

        when(customerRepository.existsByEmail("alice@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("Password123"))
                .thenReturn("encoded-password");

        authService.register(request);

        verify(passwordEncoder).encode("Password123");

        verify(customerRepository).save(argThat(customer ->
                customer.getName().equals("Alice")
                        && customer.getEmail().equals("alice@example.com")
                        && customer.getPassword().equals("encoded-password")
        ));
    }

    @Test
    void register_shouldThrowExceptionForDuplicateEmail() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setEmail("john@example.com");
        request.setPassword("Password123");

        when(customerRepository.existsByEmail("john@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(customerRepository, never())
                .save(any(Customer.class));
    }
}