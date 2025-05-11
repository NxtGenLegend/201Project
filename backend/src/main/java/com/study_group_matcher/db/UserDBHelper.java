package com.study_group_matcher.db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;
import com.study_group_matcher.model.User;
import com.study_group_matcher.model.Message;

public class UserDBHelper {

    private final Connection connection;

    // Constructor with injected connection
    public UserDBHelper(Connection connection) {
        this.connection = connection;
    }

    public void joinGroup(int groupID, User curr) throws SQLException {
        String sql = "INSERT INTO StudyGroupMembers (study_group_id, user_id, group_role, joined_date) " +
                "VALUES (?, ?, 'MEMBER', NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupID);
            stmt.setInt(2, curr.getUserId());
            stmt.executeUpdate();
        }
    }

    public void leaveGroup(int groupID, User curr) throws SQLException {
        String sql = "DELETE FROM StudyGroupMembers WHERE study_group_id = ? AND user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupID);
            stmt.setInt(2, curr.getUserId());
            stmt.executeUpdate();
        }
    }

    public void insertUser(User curr) throws SQLException {
        String sql = "INSERT INTO Users (username, password, first_name, last_name) " +
                "VALUES (?, ?, ?, ?)";

        String hashedPass = hashPassword(curr.getPassword());
        LocalDateTime loginTime = LocalDateTime.now();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, curr.getUsername());
            stmt.setString(2, hashedPass);
            stmt.setString(3, curr.getFirstName());
            stmt.setString(4, curr.getLastName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    curr.setUserId(rs.getInt(1));
                }
            }
        }
    }

    public void removeUser(User curr) throws SQLException {
        String sql = "DELETE FROM Users WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, curr.getUserId());
            stmt.executeUpdate();
        }
    }

    public void sendMessage(Message m, User curr) throws SQLException {
        String sql = "INSERT INTO Message (sender_id, user_id, message_contents) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, curr.getUserId());
            stmt.setInt(2, m.getRecipientId());
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
                    return mapResultSetToUser(rs);
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
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> getUsersByIds(List<Integer> userIds) throws SQLException {
        List<User> users = new ArrayList<>();
        if (userIds == null || userIds.isEmpty())
            return users;

        String placeholders = String.join(",", Collections.nCopies(userIds.size(), "?"));
        String sql = String.format("SELECT * FROM Users WHERE user_id IN (%s)", placeholders);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < userIds.size(); i++) {
                stmt.setInt(i + 1, userIds.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }

        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        return user;
    }

    private String hashPassword(String pass) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(pass, salt);
    }
}
