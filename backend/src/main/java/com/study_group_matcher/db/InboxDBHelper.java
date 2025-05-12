package com.study_group_matcher.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.study_group_matcher.model.Inbox;
import com.study_group_matcher.model.InboxDTO;

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

    // Updated to fix dataSource issue and constructor use
    public List<InboxDTO> getAll(String username) {
        String sql = "SELECT i.message_id, i.invitation_id, sender.username AS sender_username, " +
            "m.message_body, m.timestamp AS message_timestamp, " +
            "inv.study_group_id, sg.group_name, inv.created_at AS invitation_timestamp " +
            "FROM Inbox i " +
            "LEFT JOIN Message m ON i.message_id = m.message_id " +
            "LEFT JOIN Invitations inv ON i.invitation_id = inv.invitation_id " +
            "LEFT JOIN StudyGroup sg ON inv.study_group_id = sg.study_group_id " +
            "LEFT JOIN Users sender ON i.sender_id = sender.user_id " +
            "WHERE i.user_id = (SELECT user_id FROM Users WHERE username = ?)";
        
        List<InboxDTO> inboxItems = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InboxDTO dto = new InboxDTO(
                        rs.getObject("message_id") != null ? rs.getLong("message_id") : null,
                        rs.getObject("invitation_id") != null ? rs.getLong("invitation_id") : null,
                        rs.getString("sender_username"),
                        rs.getString("message_body"),
                        rs.getTimestamp("message_timestamp"),
                        rs.getString("group_name"),
                        rs.getTimestamp("invitation_timestamp")
                    );
                    inboxItems.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inboxItems;
    }

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
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
