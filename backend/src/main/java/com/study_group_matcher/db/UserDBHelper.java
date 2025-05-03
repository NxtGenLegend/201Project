package com.study_group_matcher.db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;
import com.study_group_matcher.model.User;
import com.study_group_matcher.model.Message;

public class UserDBHelper {
    public UserDBHelper(){
        connection = JDBCUtil.getConnection();
    }
    public UserDBHelper(Connection c){
        connection = c;
    }
    /**
     * Adds a user to a study group
     * @param groupID The ID of the group to join
     * @param curr The current user joining the group
     * @throws SQLException if database error occurs
     */
    public void joinGroup(int groupID, User curr) throws SQLException {
        String sql = "INSERT INTO StudyGroupMembers (study_group_id, user_id, group_role, joined_date) " +
                     "VALUES (?, ?, 'MEMBER', NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupID);
            stmt.setInt(2, curr.getUser_id());
            stmt.executeUpdate();
        }
    }
    
    /**
     * Removes a user from a study group
     * @param groupID The ID of the group to leave
     * @param curr The current user leaving the group
     * @throws SQLException if database error occurs
     */
    public void leaveGroup(int groupID, User curr) throws SQLException {
        String sql = "DELETE FROM StudyGroupMembers WHERE study_group_id = ? AND user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupID);
            stmt.setInt(2, curr.getUser_id());
            stmt.executeUpdate();
        }
    }
    
    /**
     * Inserts a new user into the database
     * @param curr The user to insert
     * @throws SQLException if database error occurs
     */
    public void insertUser(User curr) throws SQLException {
        String sql = "INSERT INTO Users (username, password, first_name, last_name, last_login_time) " +
                     "VALUES (?, ?, ?, ?, ?)";
        String hashedPass = hashPassword(curr.getPassword());
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(2, hashedPass); 
            stmt.setString(3, curr.getFirstName());
            stmt.setString(4, curr.getLastName());
            stmt.setString(5, stringLogin);
            stmt.executeUpdate();
            
            // Get the generated user ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    curr.setUser_id((rs.getInt(1)));
                }
            }
        }
    }
    
    /**
     * Removes a user from the database
     * @param curr The user to remove
     * @throws SQLException if database error occurs
     */
    public void removeUser(User curr) throws SQLException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, curr.getUser_id());
            stmt.executeUpdate();
        }
    }
    
    /**
     * Sends a message from a user
     * @param m The message to send
     * @param curr The user sending the message
     * @throws SQLException if database error occurs
     */
    public void sendMessage(Message m, User curr) throws SQLException {
        String sql = "INSERT INTO Message (sender_id, user_id, message_contents) " +
                     "VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, curr.getUser_id());
            stmt.setInt(2, m.getRecipientId()); // Assuming Message has getRecipientID()
            stmt.setString(3, m.getMessageContents());
            stmt.executeUpdate();
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id((rs.getInt("user_id")));
                    user.setPassword(rs.getString("password")); // Hashed password
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    return user;
                }
            }
        }
        return null;
    }
    
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id((rs.getInt("user_id")));
                    user.setPassword(rs.getString("password")); // Hashed password
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    return user;
                }
            }
        }
        return null;
    }
    
    public List<User> getUsersByIds(List<Integer> userIds) throws SQLException {
        List<User> users = new ArrayList<>();
        if (userIds == null || userIds.isEmpty()) {
            return users;
        }
        
        // Create a parameterized query with the right number of placeholders
        String placeholders = String.join(",", Collections.nCopies(userIds.size(), "?"));
        String sql = String.format("SELECT * FROM Users WHERE user_id IN (%s)", placeholders);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set each user ID parameter
            for (int i = 0; i < userIds.size(); i++) {
                stmt.setInt(i + 1, userIds.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUser_id((rs.getInt("user_id")));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    users.add(user);
                }
            }
        }
        return users;
    }
    

    private Connection connection;
    private String hashPassword(String pass){
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(pass, salt);
    }
}
