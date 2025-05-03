package com.study_group_matcher.db;

import java.sql.*;
import java.util.List;
import com.study_group_matcher.model.User;

public class AdminUserDBHelper extends UserDBHelper {

    private final Connection connect;

    // ✅ Constructor requires a Connection
    public AdminUserDBHelper(Connection connection) {
        super(connection); // also initializes UserDBHelper
        this.connect = connection;
    }

    /**
     * Adds a user to a study group (admin-only operation)
     */
    public void addToGroup(int userID, int studyGroupID) throws SQLException {
        String sql = "INSERT INTO StudyGroupMembers (study_group_id, user_id, group_role, joined_date) " +
                     "VALUES (?, ?, 'MEMBER', NOW())";

        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, studyGroupID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    /**
     * Removes a user from a study group (admin-only operation)
     */
    public void removeFromGroup(int userID, int studyGroupID) throws SQLException {
        String sql = "DELETE FROM StudyGroupMembers WHERE study_group_id = ? AND user_id = ?";

        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, studyGroupID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    /**
     * Creates a new study group with a list of users
     */
    public int createGroup(List<User> users) throws SQLException {
        int groupId = -1;

        String createGroupSql = "INSERT INTO StudyGroup (admin, privacy, max_members) " +
                                "VALUES (true, 'PUBLIC', 50)"; // Default values

        try (PreparedStatement stmt = connect.prepareStatement(createGroupSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    groupId = rs.getInt(1);
                }
            }
        }

        if (groupId == -1) {
            throw new SQLException("Failed to create study group");
        }

        String addMembersSql = "INSERT INTO StudyGroupMembers (study_group_id, user_id, group_role, joined_date) " +
                               "VALUES (?, ?, ?, NOW())";

        try (PreparedStatement stmt = connect.prepareStatement(addMembersSql)) {
            for (User user : users) {
                stmt.setInt(1, groupId);
                stmt.setInt(2, user.getUserId());
                stmt.setString(3, "MEMBER");
                stmt.addBatch();
            }
            stmt.executeBatch();
        }

        return groupId;
    }

    /**
     * Deletes a study group
     */
    public void deleteGroup(int studyGroupID) throws SQLException {
        String deleteMembersSql = "DELETE FROM StudyGroupMembers WHERE study_group_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(deleteMembersSql)) {
            stmt.setInt(1, studyGroupID);
            stmt.executeUpdate();
        }

        String deleteGroupSql = "DELETE FROM StudyGroup WHERE study_group_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(deleteGroupSql)) {
            stmt.setInt(1, studyGroupID);
            stmt.executeUpdate();
        }
    }
}