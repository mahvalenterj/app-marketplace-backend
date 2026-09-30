# App Marketplace Backend

A Spring Boot 3.2 marketplace backend application built with **Clean Architecture** principles, featuring domain-driven design, separation of concerns across four independent layers, and comprehensive test coverage.

## 🏗️ Project Structure

```
src/
├── main/java/com/marketplace/
│   ├── domain/                  # Business logic (framework-independent)
│   │   ├── entities/           # Product, Order, OrderItem
│   │   ├── repositories/       # Repository interfaces
│   │   ├── services/           # Domain services
│   │   └── exceptions/         # Custom domain exceptions
│   ├── infrastructure/         # Data access & Spring configuration
│   │   ├── persistence/
│   │   │   ├── jpa/           # JPA entities
│   │   │   └── repositories/  # Repository implementations (adapters)
│   │   └── config/            # Spring beans configuration
│   ├── application/           # Use cases & DTOs
│   │   ├── dtos/             # Request/response data transfer objects
│   │   └── usecases/         # Business workflows
│   └── api/                   # REST endpoints
│       ├── controllers/       # REST controllers
│       └── exceptions/        # Global exception handling
└── test/java/com/marketplace/
    ├── domain/               # Unit tests for domain logic
    ├── infrastructure/       # Integration tests for persistence
    └── api/                  # Controller integration tests
```

## 📋 Requirements

- **Java 17+**
- **Maven 3.9+**
- **PostgreSQL 15** (production) or H2 (testing/development)
- **Docker** (optional, for containerization)

## 🚀 Getting Started

### Local Development (H2 In-Memory Database)

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Server starts at: http://localhost:8080/api
```

### Production Setup (PostgreSQL)

Set environment variables:
```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=marketplace
DB_USER=postgres
DB_PASSWORD=yourpassword
```

Run with production profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Docker Compose

```bash
docker-compose up
```

This starts both PostgreSQL and the application with health checks.

## 🧪 Testing

### Unit Tests (Domain Layer)
```bash
mvn test -Dtest=*ServiceTest,*Test
```

### Integration Tests (Repositories & Controllers)
```bash
mvn test -Dtest=*IntegrationTest
```

### All Tests
```bash
mvn test
```

Tests use:
- **JUnit 5** for test framework
- **Mockito** for mocking dependencies
- **MockMvc** for testing REST endpoints
- **H2 in-memory database** with @DataJpaTest and @SpringBootTest

## 📚 API Endpoints

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/produtos` | Create product |
| GET | `/api/produtos/{id}` | Get product by ID |
| GET | `/api/produtos` | List all products |

**Create Product Request:**
```json
{
  "name": "Notebook",
  "description": "High-end notebook",
  "price": 2500.00,
  "quantity": 10,
  "sku": "SKU-001"
}
```

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/pedidos` | Create order |
| GET | `/api/pedidos/{id}` | Get order by ID |
| GET | `/api/pedidos` | List all orders |
| POST | `/api/pedidos/{id}/confirmar` | Confirm order |

**Create Order Request:**
```json
{
  "orderNumber": "ORD-001",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

## 🛡️ Input Validation

All request DTOs are validated using Jakarta Validation annotations:

**Product Validation:**
- `name`: Required, non-blank
- `description`: Required, non-blank
- `price`: Required, must be > 0
- `quantity`: Required, non-negative integer
- `sku`: Required, unique, non-blank

**Order Validation:**
- `orderNumber`: Required, non-blank
- `items[].productId`: Required
- `items[].quantity`: Required, minimum 1

Validation errors return 400 Bad Request with detailed error messages.

## 💼 Architecture Principles

### Clean Architecture Layers

1. **Domain Layer** - Business rules, framework-independent
2. **Infrastructure Layer** - Data access, Spring integration
3. **Application Layer** - Use cases, DTOs, coordination
4. **API Layer** - REST endpoints, exception handling

### Design Patterns Used

- **Repository Pattern** - Abstract data access
- **Adapter Pattern** - Layer boundary conversion
- **Use Case Pattern** - Business workflows
- **DTO Pattern** - Request/response transformation
- **State Machine** - Order status transitions

## 🔧 Configuration Profiles

- **default** - H2 in-memory (development)
- **test** - H2 with clean slate per test
- **prod** - PostgreSQL with environment variables

## 📦 Dependencies

- Spring Boot 3.2, Spring Data JPA
- PostgreSQL & H2
- Jakarta Validation
- JUnit 5 & Mockito

## 📄 License

MIT License
