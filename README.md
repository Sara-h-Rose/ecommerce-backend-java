\# E-Commerce Backend API



A RESTful backend application for an e-commerce system built with Java, Spring Boot, Spring Data JPA, and PostgreSQL.



The application provides APIs for managing products, customers, and orders. It includes product search and filtering, order processing with stock management, input validation, and centralized exception handling.



\## Technologies



\- Java

\- Spring Boot

\- Spring Data JPA

\- Hibernate

\- PostgreSQL

\- Maven

\- Lombok

\- Jakarta Validation



\## Features



\- Create and retrieve products

\- Search products by name

\- Filter products by category

\- Create and retrieve customers

\- Prevent duplicate customer emails

\- Create orders containing multiple products

\- Automatically update product stock when an order is placed

\- Prevent orders when stock is insufficient

\- Calculate order totals

\- Retrieve order history for a customer

\- Request validation

\- Centralized exception handling



\## API Endpoints



\### Products



| Method | Endpoint | Description |

|---|---|---|

| POST | `/products` | Create a new product |

| GET | `/products` | Get all products |

| GET | `/products/{id}` | Get a product by ID |

| GET | `/products/search?name={name}` | Search products by name |

| GET | `/products/category?category={category}` | Filter products by category |



\### Customers



| Method | Endpoint | Description |

|---|---|---|

| POST | `/customers` | Create a new customer |

| GET | `/customers` | Get all customers |

| GET | `/customers/{id}` | Get a customer by ID |

| GET | `/customers/{id}/orders` | Get all orders for a customer |



\### Orders



| Method | Endpoint | Description |

|---|---|---|

| POST | `/orders` | Create a new order |

| GET | `/orders` | Get all orders |

| GET | `/orders/{id}` | Get an order by ID |



\## Project Structure



```text

src/main/java/com/sarahrose/ecommerce/

├── customer/

│   ├── controller/

│   ├── dto/

│   ├── exception/

│   ├── model/

│   ├── repository/

│   └── service/

├── order/

│   ├── controller/

│   ├── dto/

│   ├── exception/

│   ├── model/

│   ├── repository/

│   └── service/

├── product/

│   ├── controller/

│   ├── dto/

│   ├── exception/

│   ├── model/

│   ├── repository/

│   └── service/

└── exception/

&#x20;   └── GlobalExceptionHandler.java



\## Database Configuration



The application uses PostgreSQL.



Create a PostgreSQL database named:



```text

ecommerce

```



Configure the database connection in `application.properties`:



```properties

spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce

spring.datasource.username=postgres

spring.datasource.password=${DB\_PASSWORD}



spring.jpa.hibernate.ddl-auto=update

```



The database password is provided through the `DB\_PASSWORD` environment variable instead of being stored in source control.



\### Set the Database Password



PowerShell:



```powershell

$env:DB\_PASSWORD="your\_postgresql\_password"

```



\## Running the Application



Clone the repository and navigate to the project directory.



Run the application using Maven:



```powershell

.\\mvnw spring-boot:run

```



The API will be available at:



```text

http://localhost:8080

```



\## Example: Create an Order



```http

POST /orders

Content-Type: application/json

```



```json

{

&#x20; "customerId": 1,

&#x20; "items": \[

&#x20;   {

&#x20;     "productId": 1,

&#x20;     "quantity": 2

&#x20;   },

&#x20;   {

&#x20;     "productId": 2,

&#x20;     "quantity": 1

&#x20;   }

&#x20; ]

}

```



When an order is created, the application verifies product availability, stores the order and its items, updates the product stock, and calculates the total using the product prices.



\## Error Handling



The API provides appropriate HTTP responses for common errors, including:



\- `400 Bad Request` — validation errors or insufficient stock

\- `404 Not Found` — product, customer, or order does not exist

\- `409 Conflict` — customer email already exists



\## Testing



Run the Maven test suite with:



```powershell

.\\mvnw clean test

```



Example API requests for testing the endpoints are also available in `requests.http`.

