# Task Manager API --- Production-Style Backend Service

A secure REST API for task management built with **Spring Boot**,
implementing JWT authentication, role-based access control, and user
data isolation.

This project demonstrates how a real backend service is structured in
production environments rather than a simple CRUD example.

------------------------------------------------------------------------

## Project Purpose

The goal of this project is to showcase backend engineering skills
including:

-   secure authentication architecture
-   stateless API design
-   layered backend architecture
-   access control & ownership validation
-   scalable querying using Specifications
-   production-oriented project structure

This repository represents a **portfolio-level backend application**.

------------------------------------------------------------------------

## Tech Stack

### Core

-   Java 21
-   Spring Boot
-   Spring Security
-   Spring Data JPA
-   Hibernate

### Authentication & Security

-   JWT authentication (stateless)
-   BCrypt password hashing
-   Role-Based Access Control (RBAC)

### Database

-   PostgreSQL
-   Flyway database migrations

### Infrastructure

-   Docker
-   Docker Compose
-   Gradle

### API

-   RESTful architecture
-   Pagination & filtering
-   OpenAPI / Swagger (development)

------------------------------------------------------------------------

## Architecture

Controller → Service → Repository → Database

Additional architectural components:

-   JWT Authentication Filter
-   Security Configuration Layer
-   Specification Pattern for dynamic queries
-   DTO mapping layer
-   Ownership-based authorization

------------------------------------------------------------------------

## Key Features

### Authentication

-   User registration
-   Login with JWT access tokens
-   Stateless authorization
-   Secure password storage

### Task Management

-   Create tasks
-   Update tasks
-   Delete tasks
-   Task search with filters
-   Pagination support

### Access Control

-   Users access only their own tasks
-   Admin users have global visibility
-   Endpoint-level authorization
-   Ownership validation at service level

------------------------------------------------------------------------

## Roles

### USER

-   Manage personal tasks only
-   Cannot access other users' data

### ADMIN

-   Full system access
-   Can view tasks of all users
-   Access to admin endpoints

------------------------------------------------------------------------

## Authentication Flow

1.  User registers
2.  User logs in
3.  Server issues JWT token
4.  Client sends token with requests

Example header:

Authorization: Bearer `<access_token>`{=html}

The system is fully **stateless** --- no server sessions are stored.

------------------------------------------------------------------------

## API Endpoints

### Auth

  Method   Endpoint             Description
  -------- -------------------- -------------------
  POST     /api/auth/register   Register new user
  POST     /api/auth/login      Authenticate user
  GET      /api/auth/me         Get current user

------------------------------------------------------------------------

### Tasks

  Method   Endpoint          Description
  -------- ----------------- --------------
  POST     /api/tasks        Create task
  GET      /api/tasks/{id}   Get task
  PUT      /api/tasks/{id}   Update task
  DELETE   /api/tasks/{id}   Delete task
  GET      /api/tasks        Search tasks

Supported filters: - completion status - title search - description
search - pagination - owner filtering (admin only)

------------------------------------------------------------------------

### Admin

  Method   Endpoint           Description
  -------- ------------------ ---------------------------
  GET      /api/admin/users   Search users (ADMIN only)

------------------------------------------------------------------------

## Running the Application

### Using Docker

docker compose up --build

Application runs at:

http://localhost:8080

------------------------------------------------------------------------

## Environment Configuration

Example environment variables:

APP_JWT_SECRET=`<base64-secret>`{=html}

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/repetition7
SPRING_DATASOURCE_USERNAME=postgres SPRING_DATASOURCE_PASSWORD=postgres

------------------------------------------------------------------------

## Database Migration

Database schema is managed using **Flyway**.

Migrations are executed automatically on application startup.

Location:

src/main/resources/db/migration

------------------------------------------------------------------------

## Security Design Decisions

Implemented:

-   Stateless JWT authentication
-   Custom authentication filter
-   Centralized security configuration
-   Role-based authorization
-   Ownership-based data access
-   Password hashing with BCrypt

Planned improvements:

-   Refresh tokens
-   Token revocation / logout
-   Audit logging
-   Integration security tests

------------------------------------------------------------------------

## What This Project Demonstrates

This project reflects practical backend competencies:

-   Designing secure REST APIs
-   Working with Spring Security internals
-   Implementing authorization boundaries
-   Building scalable query systems
-   Structuring maintainable backend services
-   Preparing applications for production environments

------------------------------------------------------------------------

## Future Improvements

-   Refresh token flow
-   Testcontainers integration tests
-   Event-driven architecture
-   API versioning
-   Observability (metrics & logging)
-   CI/CD pipeline

------------------------------------------------------------------------

## Author

Backend developer focused on Java & Spring ecosystem development and
production-grade API design.
