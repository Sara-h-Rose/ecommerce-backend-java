# E-Commerce Backend API

A RESTful backend application for an e-commerce system built with OpenJDK 25, Spring Boot, Spring Data JPA, and MySQL.

The application provides APIs for managing products, customers, and orders. It includes product search and filtering, order processing with stock management, input validation, CORS support for a frontend on `http://localhost:4200`, and centralized exception handling.

## Technologies

- OpenJDK 25
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok
- Jakarta Validation

## Features

- Create, update, delete, and retrieve products
- Search products by name
- Filter products by category
- Create and retrieve customers
- Prevent duplicate customer emails
- Create orders containing multiple products
- Automatically update product stock when an order is placed
- Prevent orders when stock is insufficient
- Calculate order totals
- Retrieve order history for a customer
- Request validation
- CORS enabled for `http://localhost:4200`
- Centralized exception handling

## API Endpoints

### Products

| Method | Endpoint | Description |
|---|---|---|
| POST | `/products` | Create a new product |
| GET | `/products` | Get all products |
| GET | `/products/{id}` | Get a product by ID |
| PUT | `/products/{id}` | Update a product |
| DELETE | `/products/{id}` | Delete a product |
| GET | `/products/search?name={name}` | Search products by name |
| GET | `/products/category?category={category}` | Filter products by category |

Supported categories: `ELECTRONICS`, `BOOKS`, `CLOTHING`, `HOME`, `SPORTS`, `BEAUTY`.

### Customers

| Method | Endpoint | Description |
|---|---|---|
| POST | `/customers` | Create a new customer |
| GET | `/customers` | Get all customers |
| GET | `/customers/{id}` | Get a customer by ID |
| GET | `/customers/{id}/orders` | Get all orders for a customer |

### Orders

| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders` | Create a new order |
| GET | `/orders` | Get all orders |
| GET | `/orders/{id}` | Get an order by ID |

## Project Structure

```text
src/main/java/com/sarahrose/ecommerce/
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── CustomerController.java
│   ├── OrderController.java
│   └── ProductController.java
├── service/
│   ├── CustomerService.java
│   ├── OrderService.java
│   └── ProductService.java
├── repository/
│   ├── CustomerRepository.java
│   ├── OrderRepository.java
│   └── ProductRepository.java
├── model/
│   ├── Customer.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── Product.java
├── dto/
│   ├── CustomerRequest.java
│   ├── CustomerResponse.java
│   ├── OrderItemRequest.java
│   ├── OrderItemResponse.java
│   ├── OrderRequest.java
│   ├── OrderResponse.java
│   ├── ProductRequest.java
│   └── ProductResponse.java
├── exception/
│   ├── ApiError.java
│   ├── EmailAlreadyExistsException.java
│   ├── GlobalExceptionHandler.java
│   ├── InsufficientStockException.java
│   └── ResourceNotFoundException.java
└── enums/
    └── Category.java
```

## Database Configuration

The application uses MySQL.

Create a MySQL database named:

```text
ecommerce
```

Database credentials are loaded from a local `.env` file (not committed to git).

Create a `.env` file in the project root and set your MySQL username and password:

```env
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

The datasource URL and JPA settings stay in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

## CORS

CORS is configured in `CorsConfig` to allow the frontend at `http://localhost:4200` to call the API with `GET`, `POST`, `PUT`, `DELETE`, and `OPTIONS`.

## Running the Application

Clone the repository and navigate to the project directory.

Run the application using Maven:

```powershell
.\mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Example: Create an Order

```http
POST /orders
Content-Type: application/json
```

```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

When an order is created, the application verifies product availability, stores the order and its items, updates the product stock, and calculates the total using the product prices.

## Error Handling

The API provides appropriate HTTP responses for common errors, including:

- `400 Bad Request` — validation errors or insufficient stock
- `404 Not Found` — product, customer, or order does not exist
- `409 Conflict` — customer email already exists

## Testing

Run the Maven test suite with:

```powershell
.\mvnw clean test
```

Example API requests for testing the endpoints are also available in `requests.http`.
