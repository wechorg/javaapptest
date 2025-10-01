# javaapptest
Repository with Java code to test

## Vulnerable API Test Application

This is a medium-sized Java REST API application intentionally designed with **common security vulnerabilities** for testing code scanning tools. 

**⚠️ WARNING: This application contains intentional security vulnerabilities and should NEVER be deployed in production or exposed to the internet.**

## Project Structure

```
src/main/java/com/testapp/api/
├── VulnerableApiApplication.java       # Main Spring Boot application
├── config/
│   └── SecurityConfig.java             # Configuration with hardcoded credentials
├── controller/
│   └── UserController.java             # REST API endpoints
├── model/
│   └── User.java                       # User entity
├── repository/
│   └── UserRepository.java             # Data access layer
├── service/
│   ├── AuthenticationService.java      # Authentication logic
│   ├── DataProcessingService.java      # Command execution service
│   ├── FileService.java                # File operations service
│   └── XmlProcessingService.java       # XML parsing service
└── util/
    └── LoggingUtil.java                # Logging utilities
```

## Intentional Vulnerabilities

This application contains the following security vulnerabilities for testing purposes:

### 1. SQL Injection
- **Location**: `UserRepository.java`
- User input directly concatenated into SQL queries
- Vulnerable endpoints: `/api/users/search`

### 2. Hardcoded Credentials
- **Location**: `AuthenticationService.java`, `SecurityConfig.java`
- Hardcoded passwords, API keys, and AWS credentials
- Database credentials in plain text

### 3. Path Traversal
- **Location**: `FileService.java`
- No validation of file paths allowing directory traversal
- Vulnerable endpoint: `/api/users/profile/picture`

### 4. Command Injection
- **Location**: `DataProcessingService.java`
- Direct execution of user-supplied commands
- Vulnerable endpoints: `/api/users/ping`, `/api/users/execute`

### 5. Sensitive Data Exposure in Logs
- **Location**: `LoggingUtil.java`
- Logging passwords, credit card numbers, SSNs, API keys

### 6. Weak Cryptography
- **Location**: `AuthenticationService.java`
- Use of deprecated DES encryption
- MD5 hashing for passwords
- Insecure random number generation

### 7. XML External Entity (XXE) Injection
- **Location**: `XmlProcessingService.java`
- XML parser without protection against XXE attacks

### 8. Insecure SSL/TLS Configuration
- **Location**: `SecurityConfig.java`
- Disabled certificate validation
- Disabled hostname verification

### 9. Mass Assignment Vulnerability
- **Location**: `UserController.java`
- Direct binding of user input to model objects without validation

### 10. Missing Authentication/Authorization
- **Location**: `UserController.java`
- No authentication checks on sensitive endpoints
- No authorization for delete operations

### 11. Insecure Cookie Configuration
- **Location**: `UserController.java`
- Cookies without HttpOnly and Secure flags

### 12. Unsafe Deserialization
- **Location**: `DataProcessingService.java`
- Deserializing untrusted data

## Building the Application

```bash
mvn clean compile
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/search?query=...` - Search users (SQL Injection vulnerable)
- `POST /api/users/login` - User login
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/profile/picture?filename=...` - Get profile picture (Path Traversal vulnerable)
- `GET /api/users/ping?host=...` - Ping a host (Command Injection vulnerable)
- `POST /api/users/execute?command=...` - Execute command (Command Injection vulnerable)

## Testing with Code Scanning Tools

This application is designed to be scanned by security analysis tools such as:
- CodeQL
- SonarQube
- Snyk
- OWASP Dependency Check
- Checkmarx
- Fortify

Each vulnerability should be detected by proper security scanning tools.

## License

Apache License 2.0
