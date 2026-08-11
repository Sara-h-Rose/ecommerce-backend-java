package com.sarahrose.ecommerce.controller;

import com.sarahrose.ecommerce.dto.RegisterRequest;
import com.sarahrose.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sarahrose.ecommerce.dto.LoginRequest;
import com.sarahrose.ecommerce.dto.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody @Valid RegisterRequest request) {

        authService.register(request);
    }
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody @Valid LoginRequest request) {

        return authService.login(request);
    }
}