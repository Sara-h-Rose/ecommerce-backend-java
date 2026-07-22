package com.sarahrose.ecommerce.product.service;
import com.sarahrose.ecommerce.product.dto.CreateProductRequest;
import com.sarahrose.ecommerce.product.model.Product;
import org.springframework.stereotype.Service;
import com.sarahrose.ecommerce.product.repository.ProductRepository;
import java.util.List;
import com.sarahrose.ecommerce.product.exception.ProductNotFoundException;

@Service
public class ProductService {

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public Product addProduct(CreateProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());

        return productRepository.save(product);
    }
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());

        return productRepository.save(existingProduct);
    }
}


