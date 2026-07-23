package com.sarahrose.ecommerce.product.controller;

import com.sarahrose.ecommerce.product.dto.CreateProductRequest;
import com.sarahrose.ecommerce.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import com.sarahrose.ecommerce.product.dto.ProductResponse;
import com.sarahrose.ecommerce.product.dto.UpdateProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/products")
    public ProductResponse addProduct(
            @RequestBody @Valid CreateProductRequest request) {
        return productService.addProduct(request);
    }

    @GetMapping("/products/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}