# API Gateway App

A full-stack project featuring a Spring Boot backend (API Gateway) and a React frontend, supporting secure user authentication (JWT), user-specific submission and storage of public JSON API URLs, and personalized dashboard views.

---

## Features

- JWT-based authentication
- User registration and login
- Submit and store public JSON API URLs
- Display user-specific saved API responses
- Secure token-authenticated requests

---

## Project Structure

gateway as a backend

api-gateway-app/
├── gateway/ # Spring Boot project
├── frontend/ # React project
└── README.md

---

## Tech Stack

- **Backend:** Spring Boot, Java, PostgreSQL, JPA, Spring Security
- **Frontend:** React, Axios, JavaScript
- **CI/CD:** GitLab CI
- **Deployment:** Railway (backend), Vercel (frontend)

---

## Prerequisites

- Java 17+ or 21+
- Node.js 18+ and npm
- PostgreSQL
- Maven

---

## Local Setup

### 1. Clone the Repository

git clone https://gitlab.com/abhir99-group/api-gateway-app.git

### 2. Backend (Spring Boot)

- Configure PostgreSQL connection and CORS in `backend/src/main/resources/application.properties`:

  ```
  spring.datasource.url=${DB_URL}
  spring.datasource.username=${DB_USER}
  spring.datasource.password=${DB_PASS}
  spring.datasource.driver-class-name=org.postgresql.Driver
  allowed.origin=${ALLOWED_ORIGIN}
  jwt.secret=${JWT_SECRET}
  ```

- Install dependencies and start:
  ```
  cd backend
  mvn clean install
  mvn spring-boot:run
  # Runs on http://localhost:8080 locally
  ```

### 3. Frontend (React)

- Create file `frontend/.env`:

  ```
  REACT_APP_BACKEND_URL=http://localhost:8080
  ```

- Install dependencies and start:
  ```
  cd frontend
  npm install
  npm start
  # Opens on http://localhost:3000
  ```

---

## Build (Production)

### Backend

cd gateway
mvn clean package

Output: gateway/target/api-gateway-app.jar

### Frontend

cd frontend
npm run build

Output: frontend/build directory

---

## Deployment

- **Backend:** Deploy to [Railway](https://railway.app)
  - Set environment variables: `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET`, `ALLOWED_ORIGIN`
  - Start command: `java -jar target/api-gateway-app.jar`
  - Update `allowed.origin=${ALLOWED_ORIGIN}` in `application.properties`
- **Frontend:** Deploy to [Vercel](https://vercel.com)
  - Set environment variable: `REACT_APP_BACKEND_URL=https://your-backend-url`
  - Vercel/Netlify will auto-build React apps

---

## Environment Variables

| Name                  | Context  | Example/Description               |
| --------------------- | -------- | --------------------------------- |
| REACT_APP_BACKEND_URL | Frontend | URL to deployed backend API       |
| DB_URL                | Backend  | JDBC URL for PostgreSQL           |
| DB_USER               | Backend  | PostgreSQL username               |
| DB_PASS               | Backend  | PostgreSQL password               |
| JWT_SECRET            | Backend  | Secret key for JWT                |
| ALLOWED_ORIGIN        | Backend  | Deployed frontend domain for CORS |

---

## Usage

1. **Sign up** for an account on `/signup` page.
2. **Sign in** on `/signin` page.
3. **Submit** any public JSON API URL.
4. **View** and manage your saved API responses in the dashboard.
5. Authenticated requests to backend protected endpoints via JWT.

---

## CI/CD Pipeline

Basic build and test included via GitLab CI

stages:

build

test

gateway-build:
stage: build
script:

- cd gateway
- mvn clean package

gateway-test:
stage: test
script:

- cd gateway
- mvn test

frontend-build:
stage: build
script:

- cd frontend
- npm install
- npm run build
