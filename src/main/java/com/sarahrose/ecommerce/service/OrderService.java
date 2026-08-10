package com.sarahrose.ecommerce.service;

import com.sarahrose.ecommerce.dto.OrderItemRequest;
import com.sarahrose.ecommerce.dto.OrderItemResponse;
import com.sarahrose.ecommerce.dto.OrderRequest;
import com.sarahrose.ecommerce.dto.OrderResponse;
import com.sarahrose.ecommerce.exception.AccessDeniedException;
import com.sarahrose.ecommerce.exception.InsufficientStockException;
import com.sarahrose.ecommerce.exception.ResourceNotFoundException;
import com.sarahrose.ecommerce.model.Customer;
import com.sarahrose.ecommerce.model.Order;
import com.sarahrose.ecommerce.model.OrderItem;
import com.sarahrose.ecommerce.model.Product;
import com.sarahrose.ecommerce.repository.OrderRepository;
import com.sarahrose.ecommerce.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderService(
            OrderRepository orderRepository,
            CustomerService customerService,
            ProductService productService) {

        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();

        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        Customer customer = customerService.getCustomerByEmail(currentUserEmail);

        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getProduct(itemRequest.getProductId());

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(product.getName());
            }

            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.getItems().add(orderItem);
        }

        return toOrderResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getAllOrders() {

        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        Customer customer = customerService.getCustomerByEmail(currentUserEmail);

        return orderRepository.findByCustomerId(customer.getId()).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {

        Order order = getOrder(id);

        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        if (!order.getCustomer().getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException();
        }

        return toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomer(Long customerId) {

        Customer customer = customerService.getCustomer(customerId);

        String currentUserEmail = SecurityUtil.getCurrentUserEmail();

        if (!customer.getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException();
        }

        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()))
                .toList();

        BigDecimal total = order.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                items,
                total);
    }
}
