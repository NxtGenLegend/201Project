package com.study_group_matcher.model;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // Exception handler
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String firstName = request.get("first_name");
        String lastName = request.get("last_name");
        LocalDateTime firstLogin = LocalDateTime.now();
        
        // Validation would need to be manual
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        
        try (Connection conn = JDBCUtil.getConnection()) {
            UserDBHelper userDBHelper = new UserDBHelper(conn);
            
            User newUser = new User();
            newUser.setDisplayName(username);
            newUser.setPassword(password);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setLastLoginTime(firstLogin);
            
            userDBHelper.insertUser(newUser);
            return ResponseEntity.ok("Registration successful");
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Registration failed");
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        try (Connection conn = JDBCUtil.getConnection()) {
            UserDBHelper userDBHelper = new UserDBHelper(conn);
            User user = userDBHelper.getUserByUsername(username);
            
            if (user != null && password == user.getPassword()) {
                LocalDateTime loggedIn = LocalDateTime.now();
                user.setLastLoginTime(loggedIn);
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(401).build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
