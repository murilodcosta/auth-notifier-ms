# Auth & Messaging Ecosystem 🚀

This project consists of a simplified microservices (distributed services) ecosystem developed using **Java and Spring Boot**. The main purpose is to practice and demonstrate robust authentication/authorization concepts along with event-driven asynchronous communication.

---

## 🏗️ System Architecture

The system is split into two independent services that communicate through a Message Broker:

1. **`auth-service` (Port 8080):**
    - REST API responsible for the user lifecycle (Registration and Login).
    - Robust security via **Spring Security**, password hashing using **BCrypt**, and **JWT** (JSON Web Token) issuance and validation (supporting two access levels: `USER` and `ADMIN`).
    - Isolated relational database for data persistence.
    - Acts as an event **Producer**, publishing a message to RabbitMQ whenever a new user registers.

2. **`notification-service` (Port 8081):**
    - Stateless microservice with no database attached.
    - Acts as a **Consumer**, listening to the RabbitMQ message queue asynchronously.
    - Responsible for processing new user events and simulating/sending welcome emails.

---

## 🛠️ Technologies & Tools

- **Language:** Java 21
- **Core Framework:** Spring Boot 3.x
- **Security:** Spring Security, JWT (JSON Web Token), BCrypt
- **Persistence (Auth):** Spring Data JPA & PostgreSQL
- **Messaging:** RabbitMQ & Spring AMQP
- **Documentation:** Swagger / OpenAPI
- **Infrastructure:** Docker & Docker Compose

---

## 🗺️ Data Flow (User Registration)

1. The client sends a `POST /api/auth/register` request containing the registration data.
2. The `auth-service` hashes the password, persists the user into PostgreSQL, and immediately publishes an event with the user's email and name to the RabbitMQ Exchange.
3. The `auth-service` returns a `201 Created` status to the client (without waiting for the email to be sent).
4. RabbitMQ routes the message to the appropriate queue.
5. The `notification-service`, which is monitoring the queue, consumes the message and processes the email delivery in the background.

---

## 🚀 How to Run the Infrastructure Locally

To spin up the supporting infrastructure (Database and Broker) required for local development, run the following command in the project's root directory:

```bash
docker-compose up -d
```

Available Services:

- PostgreSQL: localhost:5432 (Database: auth_db)
- RabbitMQ Management Dashboard: localhost:15672 (User: guest / Pass: guest)

---