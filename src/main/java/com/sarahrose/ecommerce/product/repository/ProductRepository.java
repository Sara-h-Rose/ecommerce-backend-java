package com.sarahrose.ecommerce.product.repository;

import com.sarahrose.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}