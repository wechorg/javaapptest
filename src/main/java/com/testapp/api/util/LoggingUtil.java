package com.testapp.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoggingUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);
    
    // VULNERABILITY: Logging sensitive information
    public void logUserLogin(String username, String password, String ipAddress) {
        logger.info("User login attempt - Username: {}, Password: {}, IP: {}", username, password, ipAddress);
    }
    
    // VULNERABILITY: Logging credit card information
    public void logPayment(String userId, String creditCardNumber, String cvv) {
        logger.info("Payment processed for user {} with card {} and CVV {}", userId, creditCardNumber, cvv);
    }
    
    // VULNERABILITY: Logging API keys and tokens
    public void logApiRequest(String endpoint, String apiKey, String authToken) {
        logger.info("API request to {} with API Key: {} and Auth Token: {}", endpoint, apiKey, authToken);
    }
    
    // VULNERABILITY: Logging personal identifiable information (PII)
    public void logUserDetails(String username, String email, String ssn, String phoneNumber) {
        logger.info("User details - Username: {}, Email: {}, SSN: {}, Phone: {}", 
                   username, email, ssn, phoneNumber);
    }
    
    // VULNERABILITY: Logging entire request parameters which may contain sensitive data
    public void logRequestParameters(Map<String, String> parameters) {
        StringBuilder logMessage = new StringBuilder("Request parameters: ");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            logMessage.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
        }
        logger.info(logMessage.toString());
    }
    
    // VULNERABILITY: Logging database connection strings with credentials
    public void logDatabaseConnection(String connectionString) {
        logger.info("Connecting to database: {}", connectionString);
    }
    
    // VULNERABILITY: Logging stack traces with sensitive information
    public void logError(Exception e, String context, String sensitiveData) {
        logger.error("Error in {}: {} - Sensitive context: {}", context, e.getMessage(), sensitiveData);
        e.printStackTrace();
    }
}
