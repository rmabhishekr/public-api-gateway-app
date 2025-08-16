# API Gateway
A full-stack project featuring a Spring Boot backend (API Gateway) and a React frontend. 
Supports user authentication (JWT), submitting public JSON API URLs, and displaying user-specific results.

---

## Features

- User authentication (JWT)
- Submit & store public JSON API URLs
- Display stored API responses in user dashboard
- Secure token-based requests

---

## Tech Stack

- **Backend:** Spring Boot, PostgreSQL, Java
- **Frontend:** React, Axios, JavaScript

---

## Prerequisites

- Java 17
- Node.js 18+ and npm 11.5.2
- PostgreSQL

---

## Setup Instructions

### 1. Clone the Repo

git clone https://gitlab.com/abhir99-group/api-gateway-app.git
cd api-gateway-app

### 2. Setup Backend

cd backend

Edit src/main/resources/application.properties for your DB config:
spring.datasource.url, spring.datasource.username, spring.datasource.password
mvn clean install
mvn spring-boot:run

**Backend runs at** `http://localhost:8080`

---

### 3. Setup Frontend  cd ../frontend
npm install

Edit .env if needed:
REACT_APP_BACKEND_URL=http://localhost:8080

**Frontend runs at** `http://localhost:3000`

---

## Build for Production

### Backend  
mvn clean package
Java JAR at: backend/target/*.jar

### Frontend

npm run build

---

## Environment Variables

- **Backend:**  
  Configure DB and JWT secret in `application.properties`.
- **Frontend:**  
  Set your backend API URL in `.env`:

---

## Usage

1. **Sign in** using a registered username: testuser and password: testpassword.
2. **Submit any public JSON API URL** in the dashboard.
3. **View your stored results.**
4. **All requests after login use your JWT (automatically added to headers).**

---

