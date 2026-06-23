# RetentionOS

RetentionOS is a customer retention platform for small businesses such as restaurants, gyms, salons, and local stores.

The goal of the platform is to help business owners identify inactive customers, generate retention messages, track trial subscriptions, and improve customer engagement.

---

## Features

### Business Management
- Create Business
- View Businesses
- Track Trial Period
- Track Subscription Status

### Customer Management
- Add Customers
- View Customers by Business
- Track Membership Expiry

### Retention Features
- Identify Inactive Customers
- Generate Personalized Retention Messages
- Dashboard Statistics

### Subscription Features
- Trial Expiry Tracking
- Subscription Expiry Tracking

### Exception Handling
- Global Exception Handling
- Custom Error Responses

### API Documentation
- Swagger / OpenAPI Integration

---

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Maven

### Database
- PostgreSQL

### Tools
- Postman
- Swagger UI
- Git & GitHub

---

## Project Structure

src/main/java/com/retentionos/backend

- controller
- service
- repository
- entity
- dto
- exception

## API Endpoints

### Business APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/businesses` | Create a new business |
| GET | `/businesses` | Get all businesses |
| GET | `/businesses/trial-expiring` | Get businesses whose trial expires soon |
| GET | `/businesses/subscription-expired` | Get businesses whose trial has expired |

### Customer APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/businesses/{businessId}/customers` | Add customer to a business |
| GET | `/businesses/{businessId}/customers` | Get customers of a business |
| GET | `/businesses/{businessId}/customers/inactive` | Get inactive customers |
| GET | `/businesses/{businessId}/customers/expiring-memberships` | Get customers with expiring memberships |

### Retention APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/businesses/{businessId}/messages` | Generate retention messages for inactive customers |
| GET | `/businesses/{businessId}/dashboard` | Get dashboard metrics for a business |

---

## API Documentation

Swagger UI is available at:

```txt
http://localhost:8080/swagger-ui/index.html