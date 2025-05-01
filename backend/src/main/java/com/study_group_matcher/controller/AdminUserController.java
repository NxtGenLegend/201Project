package com.study_group_matcher.model;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController extends UserController {
    // Exception handler
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
    }
    
   @PostMapping("/groups")
public ResponseEntity<Integer> createGroup(
    @RequestBody Map<String, Object> payload) {
    
    try (Connection conn = JDBCUtil.getConnection()) {
        AdminUserDBHelper adminDBHelper = new AdminUserDBHelper(conn);
        UserDBHelper userDBHelper = new UserDBHelper(conn);
        
        // Extract user IDs from payload
        List<Integer> userIds = (List<Integer>) payload.get("user_ids");
        
        // Get all users to add to the group
        List<User> users = userDBHelper.getUsersByIds(userIds);
        
        // Create group and get the generated ID
        int groupId = adminDBHelper.createGroup(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(groupId);
        
    } catch (ClassCastException e) {
        return ResponseEntity.badRequest().body(-1);
    } catch (SQLException e) {
        return ResponseEntity.internalServerError().body(-1);
    }
}
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable int groupId) {
        try (Connection conn = JDBCUtil.getConnection()) {
            AdminUserDBHelper adminDBHelper = new AdminUserDBHelper(conn);
            adminDBHelper.deleteGroup(groupId);
            return ResponseEntity.ok("Group deleted successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("Failed to delete group: " + e.getMessage());
        }
    }
    
    @PostMapping("/groups/{groupId}/add-user")
    public ResponseEntity<String> addUserToGroup(
            @PathVariable int groupId,
            @RequestParam int userId) {
        try (Connection conn = JDBCUtil.getConnection()) {
            AdminUserDBHelper adminDBHelper = new AdminUserDBHelper(conn);
            adminDBHelper.addToGroup(userId, groupId);
            return ResponseEntity.ok("User added to group successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("Failed to add user to group: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/groups/{groupId}/remove-user")
    public ResponseEntity<String> removeUserFromGroup(
            @PathVariable int groupId,
            @RequestParam int userId) {
        try (Connection conn = JDBCUtil.getConnection()) {
            AdminUserDBHelper adminDBHelper = new AdminUserDBHelper(conn);
            adminDBHelper.removeFromGroup(userId, groupId);
            return ResponseEntity.ok("User removed from group successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("Failed to remove user from group: " + e.getMessage());
        }
    }
}
