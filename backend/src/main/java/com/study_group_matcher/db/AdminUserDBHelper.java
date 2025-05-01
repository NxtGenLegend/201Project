package com.study_group_matcher.db;

import java.sql.*;
import java.util.List;
import com.study_group_matcher.model.User;

public class AdminUserDBHelper extends UserDBHelper {
    public AdminUserDBHelper(){
        connect = JDBCUtil.getConnection();
    }
    public AdminUserDBHelper(Connection c){
        connect = c;
    }
     /**
     * Adds a user to a study group (admin-only operation)
     * @param userID The ID of the user to add
     * @param studyGroupID The ID of the study group
     * @throws SQLException if database error occurs
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
     * @param userID The ID of the user to remove
     * @param studyGroupID The ID of the study group
     * @throws SQLException if database error occurs
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
     * @param users List of users to add to the new group
     * @return The ID of the newly created group
     * @throws SQLException if database error occurs
     */
    public int createGroup(List<User> users) throws SQLException {
        int groupId = -1;
        
        // First create the study group
        String createGroupSql = "INSERT INTO StudyGroup (admin, privacy, max_members) " +
                              "VALUES (true, 'PUBLIC', 50)"; // Default values
        
        try (PreparedStatement stmt = connect.prepareStatement(createGroupSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
            
            // Get the generated group ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    groupId = rs.getInt(1);
                }
            }
        }
        
        if (groupId == -1) {
            throw new SQLException("Failed to create study group");
        }
        
        // Then add all users to the group
        String addMembersSql = "INSERT INTO StudyGroupMembers (study_group_id, user_id, group_role, joined_date) " +
                              "VALUES (?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connect.prepareStatement(addMembersSql)) {
            for (User user : users) {
                stmt.setInt(1, groupId);
                stmt.setInt(2, user.getUser_id());
                stmt.setString(3, "MEMBER"); // Default role is member
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
        
        return groupId;
    }
    
    /**
     * Deletes a study group
     * @param studyGroupID The ID of the study group to delete
     * @throws SQLException if database error occurs
     */
    public void deleteGroup(int studyGroupID) throws SQLException {
        // First delete all members (to maintain referential integrity)
        String deleteMembersSql = "DELETE FROM StudyGroupMembers WHERE study_group_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(deleteMembersSql)) {
            stmt.setInt(1, studyGroupID);
            stmt.executeUpdate();
        }
        
        // Then delete the group
        String deleteGroupSql = "DELETE FROM StudyGroup WHERE study_group_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(deleteGroupSql)) {
            stmt.setInt(1, studyGroupID);
            stmt.executeUpdate();
        }
    }
    private Connection connect;
}
