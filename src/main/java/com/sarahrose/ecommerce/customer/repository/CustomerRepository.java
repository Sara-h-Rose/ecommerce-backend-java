package com.sarahrose.ecommerce.customer.repository;

import com.sarahrose.ecommerce.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}