package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.OrderItemRequest;
import com.sarahrose.ecommerce.dto.OrderRequest;
import com.sarahrose.ecommerce.dto.OrderResponse;
import com.sarahrose.ecommerce.exception.AccessDeniedException;
import com.sarahrose.ecommerce.exception.InsufficientStockException;
import com.sarahrose.ecommerce.model.Customer;
import com.sarahrose.ecommerce.model.Product;
import com.sarahrose.ecommerce.model.Order;
import com.sarahrose.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(4L);
        customer.setName("John");
        customer.setEmail("john@example.com");
        customer.setActive(true);

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("1000.00"));
        product.setQuantity(10);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "john@example.com",
                        null,
                        Collections.emptyList()
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createOrder_shouldCreateOrderForAuthenticatedCustomer() {

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setItems(Collections.singletonList(itemRequest));

        when(customerService.getCustomerByEmail("john@example.com"))
                .thenReturn(customer);

        when(productService.getProduct(1L))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);

        assertEquals(4L, response.customerId());
        assertEquals("John", response.customerName());

        assertEquals(8, product.getQuantity());

        verify(customerService)
                .getCustomerByEmail("john@example.com");

        verify(productService)
                .getProduct(1L);

        verify(orderRepository)
                .save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowExceptionWhenStockIsInsufficient() {

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(20);

        OrderRequest request = new OrderRequest();
        request.setItems(Collections.singletonList(itemRequest));

        when(customerService.getCustomerByEmail("john@example.com"))
                .thenReturn(customer);

        when(productService.getProduct(1L))
                .thenReturn(product);

        assertThrows(
                InsufficientStockException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals(10, product.getQuantity());

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenAccessingAnotherCustomersOrder() {

        Customer sarah = new Customer();
        sarah.setId(1L);
        sarah.setName("Sarah Updated");
        sarah.setEmail("sarah.updated@example.com");
        sarah.setActive(true);

        Order order = new Order();
        order.setId(10L);
        order.setCustomer(sarah);
        order.setOrderDate(java.time.LocalDateTime.now());

        when(orderRepository.findById(10L))
                .thenReturn(java.util.Optional.of(order));

        assertThrows(
                AccessDeniedException.class,
                () -> orderService.getOrderById(10L)
        );

        verify(orderRepository).findById(10L);
    }

    @Test
    void getOrderById_shouldReturnOwnOrder() {

        Order order = new Order();
        order.setId(10L);
        order.setCustomer(customer);
        order.setOrderDate(java.time.LocalDateTime.now());

        when(orderRepository.findById(10L))
                .thenReturn(java.util.Optional.of(order));

        OrderResponse response = orderService.getOrderById(10L);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(4L, response.customerId());
        assertEquals("John", response.customerName());

        verify(orderRepository).findById(10L);
    }
}