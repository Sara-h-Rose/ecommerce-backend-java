package com.sarahrose.ecommerce.product.service;
import com.sarahrose.ecommerce.product.dto.CreateProductRequest;
import com.sarahrose.ecommerce.product.model.Product;
import org.springframework.stereotype.Service;
import com.sarahrose.ecommerce.product.repository.ProductRepository;
import java.util.List;
import com.sarahrose.ecommerce.product.exception.ProductNotFoundException;
import com.sarahrose.ecommerce.product.dto.ProductResponse;
import com.sarahrose.ecommerce.product.dto.UpdateProductRequest;

@Service
public class ProductService {

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .toList();
    }
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public ProductResponse addProduct(CreateProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());

        Product savedProduct = productRepository.save(product);

        return toProductResponse(savedProduct);
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
    public void deleteProduct(Long id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductResponse getProductById(Long id) {
        Product product = findProductById(id);
        return toProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {

        Product existingProduct = findProductById(id);

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setCategory(request.getCategory());

        Product savedProduct = productRepository.save(existingProduct);

        return toProductResponse(savedProduct);
    }
}


