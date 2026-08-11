# E-Commerce Backend API

A RESTful backend for an e-commerce system built with OpenJDK 25, Spring Boot, Spring Data JPA, Spring Security, JWT, and MySQL.

The API supports customer registration and login, product catalog management, order placement with stock handling, and profile management. A companion Angular frontend runs at `http://localhost:4200`.

## Quick Start

1. Create a MySQL database named `ecommerce`.
2. Create a `.env` file in the project root:

```env
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_long_random_secret_key
```

3. Start the backend from the project root:

```powershell
.\mvnw spring-boot:run
```

4. (Optional) Start the Angular frontend from `../frontend`:

```powershell
npm install
npm start
```

5. Test the API with `requests.http`. Run **Auth — login** first; IntelliJ stores the JWT for the protected requests below it.

API base URL: `http://localhost:8080`

## Technologies

- OpenJDK 25
- Spring Boot
- Spring Security
- JWT
- BCrypt password hashing
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok
- Jakarta Validation

## Features

- Register and login with JWT authentication
- Passwords hashed with BCrypt before storage
- Browse, search, and filter products
- Create, update, and soft-delete products (authenticated)
- Update or soft-delete your own customer profile
- Create orders linked to the authenticated customer
- Store order item prices at purchase time
- Automatically reduce product stock when an order is placed
- Prevent orders when stock is insufficient
- Calculate order totals on the backend
- Retrieve your own orders and order history
- Request validation and centralized exception handling
- CORS enabled for `http://localhost:4200`
- 27 automated tests covering services and application startup

## Authentication and Authorization

### Public endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Create a customer account |
| POST | `/auth/login` | Login and receive a JWT |

### Protected endpoints

All non-auth endpoints require a valid JWT:

```http
Authorization: Bearer <JWT>
```

Registration is the only way to create a customer account. There is no `POST /customers`.

**What authenticated users can do today:**

| Area | Access |
|---|---|
| Products | Any authenticated user can browse, search, create, update, and soft-delete products |
| Customers | Users can read the active customer list, but may only view, update, or soft-delete their own profile |
| Orders | Users can create orders and view only their own orders |

Role-based admin authorization is **not** implemented. Product CRUD is available to every logged-in user, not just admins.

Passwords are hashed with BCrypt on registration and verified on login.

### Register

```json
{
  "name": "John",
  "email": "john@example.com",
  "password": "Password123"
}
```

### Login

```json
{
  "email": "john@example.com",
  "password": "Password123"
}
```

Response:

```json
{
  "token": "JWT_TOKEN"
}
```

## API Endpoints

### Products

| Method | Endpoint | Description | Success status |
|---|---|---|---|
| GET | `/products` | Get all active products | 200 |
| GET | `/products/{id}` | Get a product by ID | 200 |
| GET | `/products/search?name={name}` | Search products by name | 200 |
| GET | `/products/category?category={category}` | Filter products by category | 200 |
| POST | `/products` | Create a product | 200 |
| PUT | `/products/{id}` | Update a product | 200 |
| DELETE | `/products/{id}` | Soft-delete a product | 204 |

Supported categories: `ELECTRONICS`, `BOOKS`, `CLOTHING`, `HOME`, `SPORTS`, `BEAUTY`.

### Customers

| Method | Endpoint | Description |
|---|---|---|
| GET | `/customers` | Get all active customers |
| GET | `/customers/{id}` | Get your own profile |
| PUT | `/customers/{id}` | Update your own profile |
| DELETE | `/customers/{id}` | Soft-delete your own account |
| GET | `/customers/{id}/orders` | Get your own order history |

Customer update request:

```json
{
  "name": "John Updated",
  "email": "john@example.com"
}
```

### Orders

| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders` | Create an order for the authenticated customer |
| GET | `/orders` | Get your own orders |
| GET | `/orders/{id}` | Get one of your own orders |

Create order request:

```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

Do not send `customerId`. The backend determines the customer from the JWT.

Each order item stores the product price at the time of purchase, so later product price changes do not affect existing orders.

## Project Structure

```text
src/main/java/com/sarahrose/ecommerce/
├── config/
│   ├── CorsConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── CustomerController.java
│   ├── OrderController.java
│   └── ProductController.java
├── service/
│   ├── AuthService.java
│   ├── CustomerService.java
│   ├── JwtService.java
│   ├── OrderService.java
│   └── ProductService.java
├── security/
│   ├── JwtAuthenticationFilter.java
│   └── SecurityUtil.java
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
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── OrderItemRequest.java
│   ├── OrderItemResponse.java
│   ├── OrderRequest.java
│   ├── OrderResponse.java
│   ├── ProductRequest.java
│   ├── ProductResponse.java
│   └── RegisterRequest.java
├── exception/
│   ├── AccessDeniedException.java
│   ├── ApiError.java
│   ├── EmailAlreadyExistsException.java
│   ├── GlobalExceptionHandler.java
│   ├── InsufficientStockException.java
│   ├── InvalidCredentialsException.java
│   └── ResourceNotFoundException.java
└── enums/
    └── Category.java
```

## Configuration

Settings in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
spring.jpa.hibernate.ddl-auto=update
```

Run the app from the project root so Spring can load `.env`.

## CORS

CORS is configured for `http://localhost:4200` and integrated with Spring Security.

## Frontend

The Angular frontend in `../frontend` provides:

- Register and login
- Dashboard
- Product browsing
- Order placement and order history
- Profile update and account soft-delete

Product create/update/delete is not exposed in the UI; use `requests.http` or another API client with a JWT.

## Error Handling

| Status | Meaning |
|---|---|
| 400 | Validation error or insufficient stock |
| 401 | Invalid credentials or missing/invalid authentication |
| 403 | Authenticated but not allowed to access the resource |
| 404 | Resource not found |
| 409 | Conflict, such as duplicate email |

## Testing

The project includes **27 automated tests** covering auth, customer, product, and order services, plus application context startup.

Run the test suite:

```powershell
.\mvnw clean test
```

Example API requests are in `requests.http`.
