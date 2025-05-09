package com.study_group_matcher.controller;

import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import com.study_group_matcher.model.User;
import com.study_group_matcher.db.JDBCUtil;
import com.study_group_matcher.db.UserDBHelper;

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
        
        // Validation would need to be manual
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        
        try (Connection conn = JDBCUtil.getConnection()) {
            UserDBHelper userDBHelper = new UserDBHelper(conn);
            
            User newUser = new User();
            newUser.setPassword(password);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            
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
            
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(401).build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
