package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.ProductRequest;
import com.sarahrose.ecommerce.dto.ProductResponse;
import com.sarahrose.ecommerce.enums.Category;
import com.sarahrose.ecommerce.exception.ResourceNotFoundException;
import com.sarahrose.ecommerce.model.Product;
import com.sarahrose.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getProductsByCategory(Category category) {
        return productRepository.findByActiveTrueAndCategory(category).stream()
                .map(this::toProductResponse)
                .toList();
    }

    public List<ProductResponse> searchProducts(String name) {
        return productRepository.findByActiveTrueAndNameContainingIgnoreCase(name).stream()
                .map(this::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        return toProductResponse(getProduct(id));
    }

    public ProductResponse createProduct(ProductRequest request) {
        return toProductResponse(productRepository.save(toProduct(new Product(), request)));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        return toProductResponse(productRepository.save(toProduct(getProduct(id), request)));
    }

    public void deleteProduct(Long id) {

        Product product = getProduct(id);

        product.setActive(false);

        productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    private Product toProduct(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        return product;
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory()
        );
    }
}
