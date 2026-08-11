package com.sarahrose.ecommerce.model;

import com.sarahrose.ecommerce.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private Category category;
}
