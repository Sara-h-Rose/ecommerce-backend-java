package com.sarahrose.ecommerce.product.repository;

import com.sarahrose.ecommerce.product.model.Category;
import com.sarahrose.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);
}