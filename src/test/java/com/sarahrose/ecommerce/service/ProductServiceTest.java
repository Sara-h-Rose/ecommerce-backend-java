package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.ProductRequest;
import com.sarahrose.ecommerce.dto.ProductResponse;
import com.sarahrose.ecommerce.enums.Category;
import com.sarahrose.ecommerce.exception.ResourceNotFoundException;
import com.sarahrose.ecommerce.model.Product;
import com.sarahrose.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(new BigDecimal("1000.00"));
        product.setQuantity(10);
        product.setCategory(Category.ELECTRONICS);
    }

    @Test
    void getProductById_shouldReturnProduct() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse response =
                productService.getProductById(1L);

        assertEquals(1L, response.id());
        assertEquals("Laptop", response.name());
        assertEquals("Gaming laptop", response.description());
        assertEquals(new BigDecimal("1000.00"), response.price());
        assertEquals(10, response.quantity());
        assertEquals(Category.ELECTRONICS, response.category());

        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(999L)
        );

        verify(productRepository).findById(999L);
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {

        ProductRequest request = new ProductRequest();
        request.setName("Keyboard");
        request.setDescription("Mechanical keyboard");
        request.setPrice(new BigDecimal("75.00"));
        request.setQuantity(20);
        request.setCategory(Category.ELECTRONICS);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product savedProduct = invocation.getArgument(0);
                    savedProduct.setId(2L);
                    return savedProduct;
                });

        ProductResponse response =
                productService.createProduct(request);

        assertEquals(2L, response.id());
        assertEquals("Keyboard", response.name());
        assertEquals("Mechanical keyboard", response.description());
        assertEquals(new BigDecimal("75.00"), response.price());
        assertEquals(20, response.quantity());
        assertEquals(Category.ELECTRONICS, response.category());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {

        ProductRequest request = new ProductRequest();
        request.setName("Updated Laptop");
        request.setDescription("Updated description");
        request.setPrice(new BigDecimal("1200.00"));
        request.setQuantity(5);
        request.setCategory(Category.ELECTRONICS);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        ProductResponse response =
                productService.updateProduct(1L, request);

        assertEquals(1L, response.id());
        assertEquals("Updated Laptop", response.name());
        assertEquals("Updated description", response.description());
        assertEquals(new BigDecimal("1200.00"), response.price());
        assertEquals(5, response.quantity());
        assertEquals(Category.ELECTRONICS, response.category());

        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_shouldSoftDeleteProduct() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        assertFalse(product.isActive());

        verify(productRepository).findById(1L);
        verify(productRepository).save(product);

        verify(productRepository, never())
                .delete(any(Product.class));
    }

    @Test
    void getProductsByCategory_shouldReturnMatchingProducts() {

        when(productRepository.findByActiveTrueAndCategory(Category.ELECTRONICS))
                .thenReturn(List.of(product));

        List<ProductResponse> responses =
                productService.getProductsByCategory(Category.ELECTRONICS);

        assertEquals(1, responses.size());
        assertEquals("Laptop", responses.get(0).name());
        assertEquals(Category.ELECTRONICS, responses.get(0).category());

        verify(productRepository)
                .findByActiveTrueAndCategory(Category.ELECTRONICS);
    }

    @Test
    void searchProducts_shouldReturnMatchingProducts() {

        when(productRepository.findByActiveTrueAndNameContainingIgnoreCase("lap"))
                .thenReturn(List.of(product));

        List<ProductResponse> responses =
                productService.searchProducts("lap");

        assertEquals(1, responses.size());
        assertEquals("Laptop", responses.get(0).name());

        verify(productRepository)
                .findByActiveTrueAndNameContainingIgnoreCase("lap");
    }

    @Test
    void getAllProducts_shouldReturnOnlyActiveProducts() {

        when(productRepository.findByActiveTrue())
                .thenReturn(List.of(product));

        List<ProductResponse> responses =
                productService.getAllProducts();

        assertEquals(1, responses.size());

        verify(productRepository).findByActiveTrue();
        verify(productRepository, never()).findAll();
    }

    @Test
    void getProductById_shouldThrowExceptionWhenProductIsInactive() {

        product.setActive(false);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(1L)
        );

        verify(productRepository).findById(1L);
    }

}