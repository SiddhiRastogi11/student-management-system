# Student Management System API

A production-ready RESTful backend for managing student records, built with Spring Boot 3, Spring Security 6, and Spring Data JPA, backed by MySQL 8.0, and fully containerized with Docker Compose.

The API implements stateless JWT authentication, Role-Based Access Control (Admin vs. User), dynamic filtering with pagination, and interactive OpenAPI (Swagger) documentation — the core pieces of a real-world backend service.

---

## Key Features

* **Stateless JWT Authentication** — Secure login and register flow with HMAC-signed tokens
* **Role-Based Access Control** — Distinct permissions for ADMIN and USER roles
* **Dynamic Filtering & Pagination** — Query student records efficiently at scale
* **Interactive API Docs** — Swagger UI generated via Springdoc OpenAPI 3
* **Fully Containerized** — Multi-stage Docker build with Docker Compose orchestration
* **Externalized Configuration** — Environment-driven setup via `.env`

---

## Tech Stack & Architecture

| Layer             | Technology                                        |
|:------------------|:--------------------------------------------------|
| Language          | Java 17                                           |
| Framework         | Spring Boot 3, Spring Security 6, Spring Data JPA |
| Database          | MySQL 8.0                                         |
| API Documentation | Springdoc OpenAPI 3 / Swagger UI                  |
| Containerization  | Docker (multi-stage build), Docker Compose        |

---

## Environment Variables

The application uses externalized configuration. Copy `.env.example` to `.env` and set the values below:

| Variable              | Description                               | Default Fallback                      |
|:----------------------|:------------------------------------------|:--------------------------------------|
| `MYSQL_ROOT_PASSWORD` | MySQL root database password              | `rootpassword`                        |
| `DB_URL`              | JDBC connection URL                       | `jdbc:mysql://mysqldb:3306/studentdb` |
| `DB_USERNAME`         | Database username                         | `root`                                |
| `DB_PASSWORD`         | Database password                         | Injected via `MYSQL_ROOT_PASSWORD`    |
| `JWT_SECRET`          | 256-bit secret key for HMAC token signing | Pre-configured hex key                |
| `JWT_EXPIRATION`      | Token validity period (ms)                | `86400000` (24 hours)                 |

---

## Quick Start (Docker Compose)

### 1. Clone the repository
```bash
git clone https://github.com/SiddhiRastogi11/student-management-system.git
cd student-management
```

### 2. Set up environment variables
```bash
copy .env.example .env
(On macOS/Linux, use cp .env.example .env instead.)

Edit .env and fill in your values for the database credentials and JWT secret.
```

### 3. Build and run with Docker Compose
```bash
docker compose up --build -d
This starts two containers:

student-mysql-container — MySQL 8.0 database with volume persistence and health checks

student-api-container — Spring Boot application
```

### 4. Access the API
Base URL: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui/index.html

### API Overview

| Endpoint             | Method | Access        | Description                              |
|:---------------------|:-------|:--------------|:-----------------------------------------|
| `/api/auth/register` | POST   | Public        | Register a new user                      |
| `/api/auth/login`    | POST   | Public        | Authenticate and receive a JWT           |
| `/students`          | GET    | Authenticated | List all students (paginated and sorted) |
| `/students/{id}`     | GET    | Authenticated | Get a single student by ID               |
| `/students/search`   | GET    | Authenticated | Search students by name and department   |
| `/students`          | POST   | ADMIN         | Create a new student record              |
| `/students/{id}`     | PUT    | ADMIN         | Update an existing student record        |
| `/students/{id}`     | DELETE | ADMIN         | Delete a student record                  |
| `/departments`       | GET    | Authenticated | List all departments                     |
| `/departments/{id}`  | GET    | Authenticated | Get department details                   |
| `/departments`       | POST   | ADMIN         | Create a new department                  |
| `/departments/{id}`  | PUT    | ADMIN         | Update department details                |
| `/departments/{id}`  | DELETE | ADMIN         | Delete a department                      |

---

## Authentication Flow

1. Register or log in via `POST /api/auth/register` or `POST /api/auth/login`.
2. The server returns a signed JWT.
3. Include the token on subsequent requests:
```text
Authorization: Bearer <token>

```

4. Endpoints are protected based on role — ADMIN routes reject USER tokens with `403 Forbidden`.

---

## Project Structure

```text
student-management/
├── src/main/java/com/siddhi/studentmanagementsystem/
│   ├── config/         # Security, OpenAPI, and JWT configuration
│   ├── controller/     # REST controllers
│   ├── dto/            # Request/response DTOs
│   ├── entity/         # JPA entities (Student, Department, User, Role)
│   ├── exception/      # Global exception handler & custom exceptions
│   ├── repository/     # Spring Data JPA repositories
│   ├── security/       # JWT utils, user details service, and filter
│   └── service/        # Business logic layer
├── src/main/resources/
│   └── application.properties
├── postman/
│   └── Student Management System API.postman_collection.json
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md

```

## License

This project is open source and available under the MIT License.
