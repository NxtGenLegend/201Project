package com.study_group_matcher.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.study_group_matcher.model.Inbox;

public class InboxDBHelper {

    private Connection conn;
    public InboxDBHelper() {
        this.conn = JDBCUtil.getConnection();
    }

    public InboxDBHelper(Connection conn) {
        this.conn = conn;
    }

    // Add a new inbox to the database 
    public void addInbox(Inbox inbox) {
        String query = "INSERT INTO Inbox (user_id, sender_id, message_id, invitation_id) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, inbox.getUserID());
            stmt.setLong(2, inbox.getSenderID());
            stmt.setLong(3, inbox.getMessageID());
            stmt.setLong(4, inbox.getInvitationID());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inbox.setInboxID(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding inbox: " + e.getMessage());
        } finally {
            closeConnection(stmt, conn);
        }
    }

    // Get all the inboxes that belong to a specific user based on their username
    public List<Inbox> getAll(String username) {
        List<Inbox> inboxList = new ArrayList<>();
        String query = "SELECT i.* FROM Inbox i JOIN Users u ON i.user_id = u.user_id WHERE u.username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username); 
            rs = stmt.executeQuery();
    
            while (rs.next()) {
                inboxList.add(makeInbox(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeConnection(stmt, conn);
        }
    
        return inboxList;
    }

    // Create an Inbox object from a ResultSet
    private Inbox makeInbox(ResultSet rs) {
        try {
            Inbox inbox = new Inbox();

            inbox.setInboxID(rs.getLong("inbox_id"));
            inbox.setUserID(rs.getLong("user_id"));
            inbox.setSenderID(rs.getLong("sender_id"));
            inbox.setMessageID(rs.getLong("message_id"));
            inbox.setInvitationID(rs.getLong("invitation_id"));

            return inbox;

        } catch (SQLException e) {
            throw new RuntimeException("Error while creating Inbox object: " + e.getMessage());
        }
    }

    private void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(PreparedStatement stmt, Connection conn) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
