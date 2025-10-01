# Security Vulnerabilities Report

This document lists all intentional security vulnerabilities included in this application for testing purposes.

## CodeQL Detection Results

CodeQL successfully detected **13 security alerts** in this application (some vulnerability categories below contain multiple instances):

### 1. Insecure Randomness (CWE-330)
- **File**: `AuthenticationService.java`, `UserController.java`
- **Issue**: Using `java.util.Random` for security-sensitive operations
- **Impact**: Predictable tokens and session IDs
- **Line**: `UserController.java:76`

### 2. Command Injection (CWE-78)
- **Files**: `DataProcessingService.java`
- **Issue**: Executing user-provided commands without validation
- **Impact**: Remote code execution
- **Lines**: 
  - `DataProcessingService.java:14` - executeCommand method
  - `DataProcessingService.java:33` - pingHost method

### 3. Insecure TrustManager (CWE-295)
- **File**: `SecurityConfig.java`
- **Issue**: Disabled SSL certificate validation
- **Impact**: Man-in-the-middle attacks
- **Line**: `SecurityConfig.java:36`

### 4. Cross-Site Scripting (XSS) (CWE-79)
- **File**: `UserController.java`
- **Issue**: Returning user input directly in responses
- **Impact**: XSS attacks
- **Line**: `UserController.java:115`

### 5. SQL Injection (CWE-89)
- **File**: `UserRepository.java`
- **Issue**: String concatenation in SQL queries
- **Impact**: Database compromise
- **Lines**:
  - `UserRepository.java:20` - findByUsername method
  - `UserRepository.java:27` - searchUsers method

### 6. Path Injection (CWE-22)
- **File**: `FileService.java`
- **Issue**: User-controlled file paths without validation
- **Impact**: Directory traversal, arbitrary file access
- **Line**: `FileService.java:18`

### 7. Insecure Cookie (CWE-614)
- **File**: `UserController.java`
- **Issue**: Cookie without secure and httpOnly flags
- **Impact**: Session hijacking
- **Line**: `UserController.java:78`

### 8. Weak Cryptographic Algorithm (CWE-327)
- **File**: `AuthenticationService.java`
- **Issue**: Using DES encryption (deprecated and weak)
- **Impact**: Easy decryption of sensitive data
- **Lines**:
  - `AuthenticationService.java:50` - DES key
  - `AuthenticationService.java:51` - DES/ECB/PKCS5Padding cipher

### 9. Error Message Exposure (CWE-209)
- **File**: `UserController.java`
- **Issue**: Exposing detailed error messages to users
- **Impact**: Information disclosure
- **Lines**:
  - `UserController.java:122` - ping endpoint
  - `UserController.java:129` - execute endpoint

## Additional Vulnerabilities (Not Always Detected by Static Analysis)

These vulnerabilities exist in the code but may require deeper analysis or specific scanner configurations to detect:

### 10. Hardcoded Credentials (CWE-798)
- **Files**: `AuthenticationService.java`, `SecurityConfig.java`, `application.properties`
- **Issue**: Passwords, API keys, and credentials in source code
- **Examples**:
  - Admin username/password
  - AWS access keys
  - Database credentials
  - API secret keys

### 11. Sensitive Data in Logs (CWE-532)
- **File**: `LoggingUtil.java`
- **Issue**: Logging passwords, credit cards, SSNs, API keys
- **Impact**: Data breach through log files

### 12. Weak Password Hashing (CWE-916)
- **File**: `AuthenticationService.java`
- **Issue**: Using MD5 for password hashing
- **Impact**: Easy password cracking

### 13. XML External Entity (XXE) Injection (CWE-611)
- **File**: `XmlProcessingService.java`
- **Issue**: XML parser without XXE protection
- **Impact**: File disclosure, SSRF, DoS

### 14. Unsafe Deserialization (CWE-502)
- **File**: `DataProcessingService.java`
- **Issue**: Deserializing untrusted data
- **Impact**: Remote code execution

### 15. Missing Authentication/Authorization (CWE-862)
- **File**: `UserController.java`
- **Issue**: No access control on sensitive endpoints
- **Impact**: Unauthorized data access and modification

### 16. Mass Assignment (CWE-915)
- **File**: `UserController.java`
- **Issue**: Direct binding of user input to objects
- **Impact**: Privilege escalation

## Testing Recommendations

Use the following tools to scan this application:

1. **CodeQL** - Excellent detection of most vulnerabilities
2. **SonarQube** - Good for code quality and security issues
3. **Snyk** - Dependency vulnerabilities and code analysis
4. **OWASP Dependency Check** - Known vulnerable dependencies
5. **Checkmarx/Fortify** - Enterprise SAST tools
6. **Burp Suite** - Manual penetration testing

## Exploitation Examples

### SQL Injection
```bash
curl "http://localhost:8080/api/users/search?query=' OR '1'='1"
```

### Command Injection
```bash
# Note: Semicolon needs URL encoding (%3B) in the query parameter
curl "http://localhost:8080/api/users/ping?host=127.0.0.1%3B%20cat%20/etc/passwd"

# Or use the execute endpoint:
curl -X POST "http://localhost:8080/api/users/execute?command=ls%20-la"
```

### Path Traversal
```bash
curl "http://localhost:8080/api/users/profile/picture?filename=../../etc/passwd"
```

## Remediation Guide

Each vulnerability type has standard remediation approaches:

- **SQL Injection**: Use parameterized queries or ORM
- **Command Injection**: Avoid Runtime.exec(), use process builders with whitelisting
- **Path Traversal**: Validate and sanitize file paths, use Path.normalize()
- **XSS**: Encode output, use Content Security Policy
- **Weak Crypto**: Use AES-256-GCM, bcrypt for passwords
- **Hardcoded Secrets**: Use environment variables or secret management
- **XXE**: Disable external entities in XML parsers
- **Insecure Cookies**: Set Secure, HttpOnly, SameSite flags

---

**Remember**: This application is for testing purposes only. Never use these patterns in production code!
