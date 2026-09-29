# Marketplace Backend — Sprint 1

Backend de um marketplace estilo Mercado Livre, desenvolvido com **Clean Architecture** e **SOLID**.

## 📋 Visão Geral

API REST que gerencia:
- **Produtos**: criação, listagem, consulta
- **Pedidos**: criação, status, itens

**Arquitetura**: Clean Architecture + SOLID
**Stack**: Java 17, Spring Boot 3.2, PostgreSQL, JPA/Hibernate
**Testes**: JUnit 5, Mockito (80%+ coverage)

## 🏗️ Arquitetura

```
src/main/java/com/marketplace/
├── domain/                 # Lógica de negócio (independente de frameworks)
│   ├── entities/          # Entidades do domínio
│   ├── repositories/      # Interfaces de repositório
│   └── services/          # Casos de uso / serviços de domínio
├── application/           # Orquestração e DTOs
│   ├── dtos/             # Data Transfer Objects
│   └── usecases/         # Aplicação de casos de uso
├── infrastructure/        # Implementações técnicas
│   ├── persistence/      # Implementações de repositório (JPA)
│   ├── config/           # Configurações do Spring
│   └── exceptions/       # Tratamento de exceções
└── api/                  # Camada de apresentação
    └── controllers/      # REST Controllers
```

## 🚀 Quick Start

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### Local (H2 em memória)
```bash
mvn clean install
mvn spring-boot:run
```
API disponível em `http://localhost:8080/api`

### Docker Compose (PostgreSQL)
```bash
docker-compose up
```
- API: `http://localhost:8080/api`
- PostgreSQL: `localhost:5432`

## 📚 Endpoints

### Produtos
- `GET /api/produtos` — Listar todos
- `GET /api/produtos/{id}` — Obter por ID
- `POST /api/produtos` — Criar novo

### Pedidos
- `GET /api/pedidos` — Listar todos
- `POST /api/pedidos` — Criar novo
- `GET /api/pedidos/{id}` — Detalhes do pedido

## 🧪 Testes

```bash
# Rodar todos os testes
mvn test

# Com relatório de cobertura
mvn test jacoco:report
```

## 📝 CI/CD

GitHub Actions workflow em `.github/workflows/` (a implementar)

## 📄 Licença

MIT
