# CampusConnect Backend 🚀

This repository contains the backend API for the CampusConnect mobile application.

The backend is responsible for handling authentication, event management, user connections, and communication with the database. It is built using Java and Spring Boot and connects to a PostgreSQL database running in Docker.

---

## 🚀 What is This Backend?

The CampusConnect backend provides:
- REST APIs for the mobile application
- User authentication (Google Authentication & JWT)
- Event creation and participation logic
- Event-based group chat functionality
- Persistent data storage using PostgreSQL

---

## 🛠️ Tech Stack

- **Backend Framework:** Java & Spring Boot  
- **Database:** PostgreSQL  
- **Authentication:** Google Authentication & JWT  
- **Containerization:** Docker & Docker Compose  

---

## 📚 Project Status

This backend was created as part of a school project and is currently under active development.  
The application runs locally and is not deployed to a production environment.

---

## 🧪 Backend Setup (Local Development)

---

### Requirements
- Java JDK
- Docker
- Docker Compose
- Git

---

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Juangmz7/mobile-server.git
   cd mobile-server
### Environment Setup

1. Create an environment configuration file from the template:
   cp env.template .env

2. Update the environment variables as needed (database credentials, authentication settings, etc.).

### Database (PostgreSQL – Docker)

1. Start the PostgreSQL container:
   docker compose up -d

2. Verify that the database container is running before starting the backend.

### Running the Backend

1. Start the Spring Boot server:
   Windows: mvnw.cmd spring-boot:run
   macOS/Linux: ./mvnw spring-boot:run
