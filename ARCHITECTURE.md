# Clean Architecture Design

This document explains the architecture, design patterns, and data flow of the marketplace backend.

## Layer Overview

The system follows Clean Architecture principles with four independent layers:

1. **API Layer** - REST endpoints and HTTP concerns
2. **Application Layer** - Use cases and data transformation  
3. **Domain Layer** - Business logic (framework-independent)
4. **Infrastructure Layer** - Data access and Spring configuration

## Domain Layer

Core business logic with NO framework dependencies.

**Entities:**
- Product - product catalog items with quantity tracking
- Order - order aggregate with status state machine
- OrderItem - line items within orders

**Services:**
- ProductService - product creation, updates, quantity management
- OrderService - order creation, item management, status transitions

**Repositories (Interfaces):**
- ProductRepository - defines data access contract
- OrderRepository - defines order data access contract

**Exceptions:**
- DomainException - custom exception with error codes (PRODUCT_NOT_FOUND, etc)

## Infrastructure Layer

Technical implementation and Spring integration.

**JPA Entities:** ProductJpaEntity, OrderJpaEntity, OrderItemJpaEntity

**Spring Data Repositories:**
- ProductSpringDataRepository 
- OrderSpringDataRepository

**Adapters (Bridge Pattern):**
- ProductRepositoryAdapter - implements ProductRepository, converts between domain/JPA
- OrderRepositoryAdapter - implements OrderRepository, handles nested mappings

**Configuration:** BeansConfig creates service beans with dependency injection

## Application Layer

Orchestrates business workflows and coordinates layers.

**DTOs:**
- ProductRequestDto, ProductResponseDto - product transformation
- OrderRequestDto, OrderResponseDto - order transformation
- OrderItemRequestDto, OrderItemResponseDto - line item transformation

**Use Cases:**
- CreateProductUseCase, GetProductUseCase, ListProductsUseCase
- CreateOrderUseCase, GetOrderUseCase, ListOrdersUseCase, ConfirmOrderUseCase

## API Layer

REST endpoints and HTTP handling.

**Controllers:**
- ProductController - endpoints for products (/api/produtos)
- OrderController - endpoints for orders (/api/pedidos)

**Exception Handler:**
- GlobalExceptionHandler - centralized error responses
- Handles: DomainException, MethodArgumentNotValidException, generic Exception
- Returns standardized ErrorResponse JSON

## Data Flow: Creating a Product

1. Client POST /api/produtos with ProductRequestDto
2. ProductController validates input with @Valid annotation
3. Controller invokes CreateProductUseCase
4. Use case calls ProductService.createProduct()
5. Service validates business rules (name required, price > 0, SKU unique)
6. Service creates domain Product entity
7. Service calls repository.save()
8. ProductRepositoryAdapter converts Product to ProductJpaEntity
9. Spring Data JPA persists to database
10. Adapter converts JpaEntity back to domain Product
11. Use case transforms Product to ProductResponseDto
12. Controller returns 201 CREATED with response

## Design Patterns

**Repository Pattern:** Abstract data access behind interfaces
**Adapter Pattern:** Convert between domain and persistence layers
**Use Case Pattern:** Business workflow orchestration
**DTO Pattern:** Decouple API contracts from domain
**State Machine:** Order status transitions with validation
**Dependency Injection:** Spring manages all dependencies

## Testing Strategy

**Unit Tests (Domain):**
- ProductTest, OrderTest - entity logic
- ProductServiceTest, OrderServiceTest - service logic
- Uses Mockito for repository mocks
- No database, no Spring

**Integration Tests (Infrastructure):**
- ProductRepositoryIntegrationTest - JPA persistence
- OrderRepositoryIntegrationTest - ORM mapping
- Uses H2 in-memory database with @DataJpaTest

**API Tests (Controllers):**
- ProductControllerIntegrationTest - full request/response
- OrderControllerIntegrationTest - endpoint workflows
- Uses MockMvc and @SpringBootTest

## Configuration Profiles

**Default (H2 in-memory):** Local development
**Test (H2 create-drop):** Isolated test database per test
**Production (PostgreSQL):** Environment variable injection

## Key Principles

1. **Dependency Inversion:** Domain defines contracts, infrastructure implements
2. **Single Responsibility:** Each class has one reason to change
3. **Open/Closed:** Extension via new implementations, not modification
4. **Layer Independence:** Domain has zero framework knowledge
5. **Business Logic Isolation:** Use cases coordinate services
