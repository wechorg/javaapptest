package com.testapp.api.service;

import com.testapp.api.model.User;
import com.testapp.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

@Service
public class AuthenticationService {
    
    // VULNERABILITY: Hardcoded credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String API_KEY = "sk_live_51H7G8KaAbCdEfGhI9jKlMnOpQrStUvWxYz";
    private static final String DB_PASSWORD = "mySecretPassword123!";
    
    // VULNERABILITY: Weak encryption key
    private static final String ENCRYPTION_KEY = "1234567890123456";
    
    @Autowired
    private UserRepository userRepository;
    
    // VULNERABILITY: Insecure random number generation
    private Random random = new Random();
    
    public boolean authenticate(String username, String password) {
        // VULNERABILITY: Plaintext password comparison
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return true;
        }
        
        User user = userRepository.findByUsername(username).stream().findFirst().orElse(null);
        if (user != null) {
            // VULNERABILITY: Storing and comparing passwords in plaintext
            return password.equals(user.getPassword());
        }
        
        return false;
    }
    
    // VULNERABILITY: Weak cryptography - DES is deprecated
    public String encryptData(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // VULNERABILITY: Weak hash algorithm (MD5)
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // VULNERABILITY: Insecure random token generation
    public String generateToken() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }
    
    public String getApiKey() {
        return API_KEY;
    }
}
