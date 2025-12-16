# Order Service

Sistema de gerenciamento de pedidos - Recebe pedidos do Produto Externo A e disponibiliza para o Produto Externo B.

## Descrição

O Order Service é um microsserviço desenvolvido em Java 21 com Spring Boot que gerencia pedidos recebidos de sistemas externos. O serviço implementa padrões de arquitetura DDD (Domain-Driven Design), garantindo separação de responsabilidades e alta manutenibilidade.

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.8**
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **Redis** - Cache e locks distribuídos
- **RabbitMQ** - Mensageria assíncrona
- **Flyway** - Migrações de banco de dados
- **MapStruct** - Mapeamento de objetos
- **Resilience4j** - Circuit Breaker e Retry
- **Testcontainers** - Testes de integração
- **Swagger/OpenAPI** - Documentação da API

## Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker e Docker Compose
- PostgreSQL 15+ (ou via Docker)
- Redis 7+ (ou via Docker)
- RabbitMQ 3+ (ou via Docker)

## Como Executar

### 1. Iniciar serviços com Docker Compose

```bash
docker-compose up -d
```

Isso iniciará:

- PostgreSQL na porta 5432
- Redis na porta 6379
- RabbitMQ na porta 5672 (Management UI na porta 15672)

### 2. Executar a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### 3. Acessar documentação Swagger

```
http://localhost:8080/swagger-ui.html
```

## Endpoints da API

### POST /api/orders

Cria um novo pedido.

**Request:**

```json
{
  "externalId": "EXT-123",
  "products": [
    {
      "name": "Produto A",
      "value": 10.5,
      "quantity": 2
    }
  ]
}
```

**Response:** 201 Created

```json
{
  "id": 1,
  "externalId": "EXT-123",
  "totalValue": 21.00,
  "status": "PROCESSED",
  "createdAt": "2024-01-01T10:00:00",
  "processedAt": "2024-01-01T10:00:00",
  "products": [...]
}
```

### GET /api/orders/{id}

Busca um pedido por ID.

**Response:** 200 OK

```json
{
  "id": 1,
  "externalId": "EXT-123",
  "totalValue": 21.00,
  "status": "PROCESSED",
  ...
}
```

### GET /api/orders

Lista todos os pedidos com paginação.

**Query Parameters:**

- `page` - Número da página (default: 0)
- `size` - Tamanho da página (default: 20)

### GET /api/orders/status/{status}

Filtra pedidos por status (PENDING, PROCESSED, FAILED).

## Funcionalidades

### Duplicação de Pedidos

- Verificação de duplicação usando Redis com TTL de 24 horas
- Prevenção de processamento duplicado do mesmo `externalId`

### Locks Distribuídos

- Controle de concorrência usando Redis
- Prevenção de race conditions em ambientes distribuídos

### Eventos Assíncronos

- Publicação de eventos via RabbitMQ quando pedido é processado
- Exchange: `order.exchange`
- Queue: `order.queue`
- Routing Key: `order.processed`

### Circuit Breaker e Retry

- Configuração de Resilience4j para chamadas externas
- Retry automático em caso de falhas temporárias
- Circuit breaker para proteger contra falhas em cascata

### Optimistic Locking

- Controle de concorrência no banco de dados
- Prevenção de atualizações simultâneas

## Testes

### Executar todos os testes

```bash
mvn test
```

### Executar testes de integração

```bash
mvn verify
```

### Cobertura de código

```bash
mvn test jacoco:report
```

## Configuração

As configurações estão em `src/main/resources/application.properties`:

- **Database**: PostgreSQL
- **Cache**: Redis
- **Messaging**: RabbitMQ
- **Actuator**: Health checks e métricas

## Monitoramento

### Health Checks

```
GET /actuator/health
```

### Métricas Prometheus

```
GET /actuator/prometheus
```

## Desenvolvimento

### Compilar o projeto

```bash
mvn clean compile
```

### Executar testes

```bash
# Executar apenas testes unitários (não requer Docker) - RECOMENDADO
mvn clean install

# Executar todos os testes incluindo integração (requer Docker rodando)
mvn clean install -DskipITs=false

# Executar apenas testes unitários explicitamente
mvn test

# Executar apenas testes de integração (requer Docker rodando)
mvn verify -DskipTests=true -DskipITs=false
```

**Nota**: 
- Por padrão, os testes de integração são **pulados automaticamente** (`skipITs=true`) para permitir builds sem Docker.
- Os testes unitários (`mvn test` ou `mvn clean install`) não requerem Docker e podem ser executados em qualquer ambiente.
- Os testes de integração que usam Testcontainers requerem Docker rodando. Para executá-los, use `-DskipITs=false`.
- Para executar todos os testes incluindo integração: `mvn clean install -DskipITs=false` (requer Docker).

### Gerar JAR

```bash
mvn clean package
```

## Licença

Este projeto é um teste técnico para AMbev.
