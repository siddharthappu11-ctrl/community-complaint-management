# Community Complaint Management System (WardConnect)

A full-stack web application built with **Java 17**, **Spring Boot 3**, **Spring Security (JWT)**, **Spring Data MongoDB**, and a responsive Vanilla HTML/CSS/JS frontend.

---

## 📋 Prerequisites

1. **Java JDK 17** or higher installed.
2. **MongoDB Community Server** running locally on `localhost:27017`.
3. **Apache Maven 3.8+** (or use bundled Maven binaries at `C:\tools\apache-maven-3.9.6\bin\mvn.cmd`).

---

## 🚀 How to Run the Application

### 1. Ensure MongoDB is Running
MongoDB runs on default port `27017` with database name `community_complaints`.
- **Windows Service**: Start MongoDB service via Services or command line:
  ```powershell
  Start-Service -Name MongoDB
  ```
- **MongoDB Compass / Shell Connection URI**: `mongodb://localhost:27017/community_complaints`

### 2. Run the Spring Boot Application
From the project root folder:
```powershell
& "C:\tools\apache-maven-3.9.6\bin\mvn.cmd" spring-boot-run
# OR if Maven is added to PATH:
mvn spring-boot:run
```

### 3. Open in Browser
Once the server starts up on port `8080`, navigate to:
- 🌐 **Full Application URL**: [http://localhost:8080](http://localhost:8080)
- 🔑 **Resident Login**: [http://localhost:8080/resident-login.html](http://localhost:8080/resident-login.html)
- 🛡️ **Admin Portal**: [http://localhost:8080/admin-login.html](http://localhost:8080/admin-login.html)

---

## 👤 Demo Credentials

The system automatically seeds the database on first startup if empty:

| Role | Email | Password | Name |
|---|---|---|---|
| **Admin** | `admin@ward.com` | `admin123` | Ward Administrator |
| **Resident** | `maya@example.com` | `demo123` | Maya Rao |
| **Resident** | `arun@example.com` | `demo123` | Arun Kapoor |

---

## 🎓 OOP Concepts Used (Object-Oriented Programming Coursework)

This project strictly adheres to Java OOP principles without using Lombok boilerplate annotations. All getters, setters, constructors, and `toString()` methods are explicitly written by hand.

### 1. Encapsulation
- **Description**: Private instance fields exposed via public getters and setters.
- **Classes**:
  - [`User.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/User.java)
  - [`Complaint.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/Complaint.java)
  - [`RegisterRequest.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/dto/RegisterRequest.java)
  - [`LoginRequest.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/dto/LoginRequest.java)
  - [`AuthResponse.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/dto/AuthResponse.java)

### 2. Abstraction
- **Description**: Abstract class `User` defines base fields and abstract methods `getRole()` and `getDashboardPath()`. Interfaces `AuthService` and `ComplaintService` declare business contracts.
- **Classes & Files**:
  - Abstract Class: [`User.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/User.java)
  - Service Interfaces: [`AuthService.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/AuthService.java), [`ComplaintService.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/ComplaintService.java)

### 3. Inheritance
- **Description**: Concrete classes `Resident` and `Admin` extend the abstract class `User`. They share base user properties (`id`, `name`, `email`, `passwordHash`, `createdAt`) and are persisted in a single MongoDB `"users"` collection using Spring Data type aliases (`@TypeAlias("RESIDENT")`, `@TypeAlias("ADMIN")`).
- **Classes & Files**:
  - [`Resident.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/Resident.java) (`extends User`)
  - [`Admin.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/Admin.java) (`extends User`)

### 4. Polymorphism
- **Method Overriding**: Subclasses `Resident` and `Admin` override `getRole()` and `getDashboardPath()` from `User`.
  - Files: [`Resident.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/Resident.java), [`Admin.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/Admin.java)
- **Method Overloading**: `ComplaintService` and `ComplaintServiceImpl` provide overloaded versions of `findComplaints()`:
  - `findComplaints()` (retrieve all)
  - `findComplaints(ComplaintStatus status, Category category, String searchQuery)` (filtered search)
  - Files: [`ComplaintService.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/ComplaintService.java), [`ComplaintServiceImpl.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/impl/ComplaintServiceImpl.java)

### 5. Interfaces & Implementation Classes
- **Description**: Decoupled service layer architecture using interface contracts and implementation classes.
- **Classes & Files**:
  - [`AuthService.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/AuthService.java) ➔ [`AuthServiceImpl.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/impl/AuthServiceImpl.java)
  - [`ComplaintService.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/ComplaintService.java) ➔ [`ComplaintServiceImpl.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/impl/ComplaintServiceImpl.java)

### 6. Constructors & toString()
- **Description**: Default and parameterized constructors implemented across domain models and DTOs along with overridden `toString()` for object representation.
- **Files**: [`User.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/User.java), [`Complaint.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/model/Complaint.java)

### 7. Enums
- **Description**: Strongly-typed enumerations for system roles, complaint statuses, categories, and priorities with JSON creators/values.
- **Files**:
  - [`Role.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/enums/Role.java) (`RESIDENT`, `ADMIN`)
  - [`ComplaintStatus.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/enums/ComplaintStatus.java) (`PENDING`, `IN_PROGRESS`, `RESOLVED`)
  - [`Category.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/enums/Category.java) (`WATER`, `ROADS`, `STREETLIGHTS`, `GARBAGE`, `DRAINAGE`, `NOISE`)
  - [`Priority.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/enums/Priority.java) (`LOW`, `MEDIUM`, `HIGH`)

### 8. Custom Exception Handling
- **Description**: Custom runtime exceptions handled by a centralized `@RestControllerAdvice` returning structured JSON error payloads.
- **Files**:
  - Custom Exceptions: [`ResourceNotFoundException.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/exception/ResourceNotFoundException.java), [`InvalidCredentialsException.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/exception/InvalidCredentialsException.java), [`DuplicateEmailException.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/exception/DuplicateEmailException.java), [`UnauthorizedException.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/exception/UnauthorizedException.java)
  - Global Advice: [`GlobalExceptionHandler.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/exception/GlobalExceptionHandler.java)

### 9. Collections and Streams API
- **Description**: Heavy use of `List`, `Map`, `Set`, and Java Streams for filtering, searching, grouping, and statistical aggregation.
- **Files**: [`ComplaintServiceImpl.java`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/src/main/java/com/wardconnect/ccms/service/impl/ComplaintServiceImpl.java) (see `getPublicStats()`, `getAdminStats()`, and `findComplaints()`)

---

## 📡 REST API Specifications

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/resident/register` | Public | Register a new resident account |
| `POST` | `/api/auth/resident/login` | Public | Authenticate resident and receive JWT |
| `POST` | `/api/auth/admin/login` | Public | Authenticate admin staff and receive JWT |
| `GET` | `/api/public/stats` | Public | Get aggregate complaint stats for home page |
| `GET` | `/api/public/track/{trackingId}` | Public | Track a complaint status by tracking ID |
| `POST` | `/api/complaints` | Resident | Submit a new complaint |
| `GET` | `/api/complaints/my` | Resident | Fetch all complaints filed by logged-in resident |
| `GET` | `/api/admin/complaints` | Admin | Filter/search all complaints (`?status=`, `?category=`, `?q=`) |
| `PUT` | `/api/admin/complaints/{id}` | Admin | Update complaint status and add admin response |
| `GET` | `/api/admin/stats` | Admin | Get operations console dashboard summary stats |

---

## 🧪 Testing

Execute automated unit tests with Maven:
```powershell
& "C:\tools\apache-maven-3.9.6\bin\mvn.cmd" test
```
Sample REST requests are available in [`test.http`](file:///c:/Users/Siddharth/OneDrive/Desktop/community%20complaint%20management/test.http).
#   c o m m u n i t y - c o m p l a i n t - m a n a g e m e n t  
 