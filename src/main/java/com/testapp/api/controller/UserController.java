package com.testapp.api.controller;

import com.testapp.api.model.User;
import com.testapp.api.repository.UserRepository;
import com.testapp.api.service.AuthenticationService;
import com.testapp.api.service.DataProcessingService;
import com.testapp.api.service.FileService;
import com.testapp.api.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticationService authService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private DataProcessingService dataProcessingService;
    
    @Autowired
    private LoggingUtil loggingUtil;
    
    // VULNERABILITY: SQL Injection via search parameter
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userRepository.searchUsers(query);
        return ResponseEntity.ok(users);
    }
    
    // VULNERABILITY: Exposed sensitive user data
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id);
        return ResponseEntity.ok(user);
    }
    
    // VULNERABILITY: No authentication/authorization check
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.searchUsers("");
        return ResponseEntity.ok(users);
    }
    
    // VULNERABILITY: Logging sensitive information
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String username, 
            @RequestParam String password,
            @RequestParam(required = false) String ipAddress,
            HttpServletResponse response) {
        
        loggingUtil.logUserLogin(username, password, ipAddress);
        
        boolean authenticated = authService.authenticate(username, password);
        Map<String, String> result = new HashMap<>();
        
        if (authenticated) {
            String token = authService.generateToken();
            
            // VULNERABILITY: Cookie without HttpOnly and Secure flags
            Cookie cookie = new Cookie("auth_token", token);
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            
            result.put("status", "success");
            result.put("token", token);
        } else {
            result.put("status", "failed");
        }
        
        return ResponseEntity.ok(result);
    }
    
    // VULNERABILITY: Mass assignment vulnerability
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }
    
    // VULNERABILITY: No input validation
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userRepository.update(user);
        return ResponseEntity.ok("User updated successfully");
    }
    
    // VULNERABILITY: No authorization check for deletion
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userRepository.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    
    // VULNERABILITY: Path traversal via file parameter
    @GetMapping("/profile/picture")
    public ResponseEntity<String> getProfilePicture(@RequestParam String filename) {
        String content = fileService.readFile(filename);
        return ResponseEntity.ok(content);
    }
    
    // VULNERABILITY: Command injection via ping endpoint
    @GetMapping("/ping")
    public ResponseEntity<String> pingServer(@RequestParam String host) {
        String result = dataProcessingService.pingHost(host);
        return ResponseEntity.ok(result);
    }
    
    // VULNERABILITY: Executing arbitrary commands
    @PostMapping("/execute")
    public ResponseEntity<String> executeCommand(@RequestParam String command) {
        String result = dataProcessingService.executeCommand(command);
        return ResponseEntity.ok(result);
    }
}
