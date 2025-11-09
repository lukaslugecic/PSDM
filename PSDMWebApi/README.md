# 🌐 PSDMWebApi – RESTful Backend API

## 🔍 Overview
The **PSDM Web API** provides backend services for the PSDM mobile app.  
It is developed using **Spring Boot**, with layered architecture that handles authentication, business logic, and data persistence.

## 🧩 Technologies Used
- **Language:** Java  
- **Framework:** Spring Boot  
- **ORM:** Spring Data JPA  
- **Authentication:** Keycloak (OAuth 2.0 / OpenID Connect)  
- **Database:** PostgreSQL  
- **Build Tool:** Maven  
- **Architecture:** Layered (Controller → Service → Repository)

## 🧱 Architecture Overview
- **Presentation Layer:** REST controllers managing HTTP requests and responses  
- **Service Layer:** Business logic, validation, and transformation  
- **Data Layer:** Repositories for database operations via JPA  
- **IAM Integration:** Keycloak handles authentication tokens and user roles  

## 🧠 Key Features
- User management with Keycloak integration  
- CRUD operations for problems, sessions, solutions, and votes  
- Support for multiple collaborative and decision-making techniques  
- Role-based authorization (Admin, Moderator, Participant)  
- Well-defined REST endpoints returning JSON data  

## 🔗 Related Repository
➡️ **Mobile Client:** [PSDMClientApp](https://github.com/lukaslugecic/PSDM/tree/master/PSDMClientApp)

## ⚙️ Installation & Setup
```bash
# Clone repository
git clone https://github.com/lukaslugecic/PSDMWebApi.git
cd PSDMWebApi

# Configure application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/psdm
spring.datasource.username=postgres
spring.datasource.password=your_password

# Configure Keycloak
keycloak.realm=psdm
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.resource=psdm-webapi
keycloak.credentials.secret=your_client_secret

# Build and run
mvn spring-boot:run
```

## 🧩 Example Endpoints
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/api/problems` | Create a new problem |
| `GET`  | `/api/problems` | Get all problems |
| `POST` | `/api/sessions` | Create new session |
| `GET`  | `/api/solutions` | Retrieve all solutions |
| `POST` | `/api/votes` | Submit a vote |

## 🗃️ Database Schema
The database includes entities such as:
- **User**, **Role**  
- **Problem**, **Session**, **Solution**  
- **Vote**, **Attribute**, **DecisionMethod**, **ProblemSolvingMethod**  

All relationships are modeled with foreign keys to support multi-user collaboration and session tracking.
