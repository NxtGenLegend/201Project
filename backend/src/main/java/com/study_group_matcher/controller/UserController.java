package com.study_group_matcher.controller;

import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import java.sql.PreparedStatement;
import com.study_group_matcher.model.User;
import com.study_group_matcher.db.JDBCUtil;
import com.study_group_matcher.db.UserDBHelper;


@CrossOrigin(origins = "*") // Optional: allow cross-origin requests from frontend
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

            System.out.println("Provided password: " + password);
            System.out.println("Stored hash: " + user.getPassword());
            System.out.println(BCrypt.checkpw(password, user.getPassword()));
            
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(401).build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //API Endpoint to insert users who want to join a study group
    @PostMapping("/{groupId}/join-request")
    public ResponseEntity<String> requestToJoinGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        try (Connection conn = JDBCUtil.getConnection()) {
            // You can insert a row into a 'JoinRequests' table or send a pending invitation
            String sql = "INSERT INTO JoinRequests (study_group_id, user_id, status) VALUES (?, ?, 'PENDING')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, groupId);
                stmt.setLong(2, userId);
                stmt.executeUpdate();
            }

            return ResponseEntity.ok("Join request submitted.");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error submitting join request: " + e.getMessage());
        }
    }

    //API endpoint to get the userID from userName
    @GetMapping("/by-username")
    public ResponseEntity<Long> getUserIdByUsername(@RequestParam String username) {
        try (Connection conn = JDBCUtil.getConnection()) {
            UserDBHelper userDBHelper = new UserDBHelper(conn);
            User user = userDBHelper.getUserByUsername(username);

            if (user != null) {
                return ResponseEntity.ok((long) user.getUserId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
