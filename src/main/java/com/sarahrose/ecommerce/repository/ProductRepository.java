package com.sarahrose.ecommerce.repository;

import com.sarahrose.ecommerce.enums.Category;
import com.sarahrose.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByActiveTrueAndCategory(Category category);

    List<Product> findByActiveTrueAndNameContainingIgnoreCase(String name);
}
