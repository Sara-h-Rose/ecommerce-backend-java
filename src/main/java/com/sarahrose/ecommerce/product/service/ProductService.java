package com.sarahrose.ecommerce.product.service;
import com.sarahrose.ecommerce.product.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import com.sarahrose.ecommerce.product.exception.ProductNotFoundException;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();

    public List<Product> getAllProducts(){
        return products;
    }

    public Product addProduct(Product product){
        products.add(product);
        return product;
    }
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        products.remove(product);
    }

    public Product getProductById(Long id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }

        throw new ProductNotFoundException(id);
    }
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());

        return existingProduct;
    }
}


