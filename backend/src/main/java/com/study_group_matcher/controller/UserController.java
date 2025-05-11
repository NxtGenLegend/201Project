package com.study_group_matcher.controller;

import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


@CrossOrigin(origins = "*")
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
        
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        
        try (Connection conn = JDBCUtil.getConnection()) {
            UserDBHelper userDBHelper = new UserDBHelper(conn);
            
            // Check if username exists
            if (userDBHelper.getUserByUsername(username) != null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            
            User newUser = new User();
            newUser.setUsername(username);  // THIS WAS MISSING LMAO WHOOPS
            newUser.setPassword(password);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            
            userDBHelper.insertUser(newUser);
            return ResponseEntity.ok("Registration successful");
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error for debugging
            return ResponseEntity.internalServerError()
                .body("Registration failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        try (Connection conn = JDBCUtil.getConnection()) {
            UserDBHelper userDBHelper = new UserDBHelper(conn);
            User user = userDBHelper.getUserByUsername(username);
            
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(401).build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


