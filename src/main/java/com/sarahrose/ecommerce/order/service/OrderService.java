package com.sarahrose.ecommerce.order.service;
import com.sarahrose.ecommerce.customer.exception.CustomerNotFoundException;
import com.sarahrose.ecommerce.customer.model.Customer;
import com.sarahrose.ecommerce.customer.repository.CustomerRepository;
import com.sarahrose.ecommerce.order.dto.CreateOrderRequest;
import com.sarahrose.ecommerce.order.dto.OrderResponse;
import com.sarahrose.ecommerce.order.model.Order;
import com.sarahrose.ecommerce.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.sarahrose.ecommerce.order.model.OrderItem;
import java.time.LocalDateTime;
import com.sarahrose.ecommerce.order.exception.OrderNotFoundException;
import com.sarahrose.ecommerce.product.exception.ProductNotFoundException;
import com.sarahrose.ecommerce.product.model.Product;
import com.sarahrose.ecommerce.product.repository.ProductRepository;
import com.sarahrose.ecommerce.order.dto.OrderItemRequest;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException(request.getCustomerId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new ProductNotFoundException(itemRequest.getProductId()));

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.getItems().add(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getCustomer().getId(),
                order.getCustomer().getName()
        );
    }
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toOrderResponse)
                .toList();
    }
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id));

        return toOrderResponse(order);
    }
}