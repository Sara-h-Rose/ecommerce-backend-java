package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.LoginRequest;
import com.sarahrose.ecommerce.dto.LoginResponse;
import com.sarahrose.ecommerce.dto.RegisterRequest;
import com.sarahrose.ecommerce.exception.EmailAlreadyExistsException;
import com.sarahrose.ecommerce.exception.ResourceNotFoundException;
import com.sarahrose.ecommerce.model.Customer;
import com.sarahrose.ecommerce.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sarahrose.ecommerce.exception.InvalidCredentialsException;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        customer.setPassword(hashedPassword);

        customerRepository.save(customer);
    }

    public LoginResponse login(LoginRequest request) {

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!customer.isActive()) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                customer.getPassword())) {

            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(customer.getEmail());

        return new LoginResponse(token);
    }
}