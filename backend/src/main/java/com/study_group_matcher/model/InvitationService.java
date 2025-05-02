package com.study_group_matcher.model;

import com.study_group_matcher.db.JDBCUtil;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvitationService {
    
    public List<InvitationDTO> getPendingInvitationsForUser(Long userId) {
        List<InvitationDTO> invitations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT i.invitation_id, i.study_group_id, i.user_id, i.status, i.created_at, " +
                        "sg.study_group_id, sg.admin_id, u.first_name, u.last_name " +
                        "FROM Invitations i " +
                        "JOIN StudyGroup sg ON i.study_group_id = sg.study_group_id " +
                        "JOIN Users u ON i.user_id = u.user_id " +
                        "WHERE i.user_id = ? AND i.status = 'PENDING'";
            
            ps = conn.prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                InvitationDTO dto = new InvitationDTO();
                dto.setId(rs.getLong("invitation_id"));
                dto.setGroupId(rs.getLong("study_group_id"));
                dto.setGroupName("Study Group " + rs.getLong("study_group_id")); // Placeholder - enhance as needed
                dto.setRecipientId(rs.getLong("user_id"));
                dto.setRecipientName(rs.getString("first_name") + " " + rs.getString("last_name"));
                dto.setStatus(Invitation.InvitationStatus.valueOf(rs.getString("status")));
                invitations.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pending invitations: " + e.getMessage(), e);
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }
        
        return invitations;
    }
    
    public InvitationDTO createInvitation(Long groupId, Long recipientId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = JDBCUtil.getConnection();
            
            // Check if invitation already exists
            String checkSql = "SELECT COUNT(*) FROM Invitations WHERE study_group_id = ? AND user_id = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setLong(1, groupId);
            ps.setLong(2, recipientId);
            rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new RuntimeException("Invitation already exists");
            }
            
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            
            // Check if study group exists
            String checkGroupSql = "SELECT COUNT(*) FROM StudyGroup WHERE study_group_id = ?";
            ps = conn.prepareStatement(checkGroupSql);
            ps.setLong(1, groupId);
            rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new RuntimeException("Study group not found");
            }
            
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            
            // Check if user exists
            String checkUserSql = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            ps = conn.prepareStatement(checkUserSql);
            ps.setLong(1, recipientId);
            rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new RuntimeException("User not found");
            }
            
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            
            // Insert new invitation
            String insertSql = "INSERT INTO Invitations (study_group_id, user_id, status, created_at) VALUES (?, ?, 'PENDING', ?)";
            ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, groupId);
            ps.setLong(2, recipientId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            long invitationId = 0;
            if (rs.next()) {
                invitationId = rs.getLong(1);
            } else {
                throw new RuntimeException("Failed to get generated key for new invitation");
            }
            
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            
            // Get user name
            String userSql = "SELECT first_name, last_name FROM Users WHERE user_id = ?";
            ps = conn.prepareStatement(userSql);
            ps.setLong(1, recipientId);
            rs = ps.executeQuery();
            
            String firstName = "";
            String lastName = "";
            if (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }
            
            // Create and return DTO
            InvitationDTO dto = new InvitationDTO();
            dto.setId(invitationId);
            dto.setGroupId(groupId);
            dto.setGroupName("Study Group " + groupId); // Placeholder - enhance as needed
            dto.setRecipientId(recipientId);
            dto.setRecipientName(firstName + " " + lastName);
            dto.setStatus(Invitation.InvitationStatus.PENDING);
            
            return dto;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error creating invitation: " + e.getMessage(), e);
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }
    }
    
    public InvitationDTO acceptInvitation(Long invitationId, Long userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = JDBCUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Get and validate invitation
            Invitation invitation = getInvitation(conn, invitationId);
            
            if (invitation == null) {
                throw new RuntimeException("Invitation not found");
            }
            
            if (!invitation.getRecipientId().equals(userId)) {
                throw new RuntimeException("User is not the recipient of this invitation");
            }
            
            if (invitation.getStatus() != Invitation.InvitationStatus.PENDING) {
                throw new RuntimeException("Invitation is not in PENDING state");
            }
            
            // Update invitation status
            String updateSql = "UPDATE Invitations SET status = 'ACCEPTED', updated_at = ? WHERE invitation_id = ?";
            ps = conn.prepareStatement(updateSql);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, invitationId);
            ps.executeUpdate();
            
            JDBCUtil.close(ps);
            
            // Add user to the study group
            String insertMemberSql = "INSERT INTO StudyGroupMembers (study_group_id, user_id, group_role) VALUES (?, ?, 'MEMBER')";
            ps = conn.prepareStatement(insertMemberSql);
            ps.setLong(1, invitation.getGroupId());
            ps.setLong(2, userId);
            ps.executeUpdate();
            
            conn.commit(); // Commit transaction
            
            // Update the invitation object
            invitation.accept();
            
            // Return updated DTO
            return getInvitationDTO(conn, invitation);
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction on error
                }
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Error during rollback: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Error accepting invitation: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException autoCommitEx) {
                System.err.println("Error resetting auto-commit: " + autoCommitEx.getMessage());
            }
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }
    }
    
    public InvitationDTO declineInvitation(Long invitationId, Long userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = JDBCUtil.getConnection();
            
            // Get and validate invitation
            Invitation invitation = getInvitation(conn, invitationId);
            
            if (invitation == null) {
                throw new RuntimeException("Invitation not found");
            }
            
            if (!invitation.getRecipientId().equals(userId)) {
                throw new RuntimeException("User is not the recipient of this invitation");
            }
            
            if (invitation.getStatus() != Invitation.InvitationStatus.PENDING) {
                throw new RuntimeException("Invitation is not in PENDING state");
            }
            
            // Update invitation status
            String updateSql = "UPDATE Invitations SET status = 'DECLINED', updated_at = ? WHERE invitation_id = ?";
            ps = conn.prepareStatement(updateSql);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, invitationId);
            ps.executeUpdate();
            
            // Update the invitation object
            invitation.decline();
            
            // Return updated DTO
            return getInvitationDTO(conn, invitation);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error declining invitation: " + e.getMessage(), e);
        } finally {
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }
    }
    
    private Invitation getInvitation(Connection conn, Long invitationId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT * FROM Invitations WHERE invitation_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, invitationId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Invitation invitation = new Invitation();
                invitation.setId(rs.getLong("invitation_id"));
                invitation.setGroupId(rs.getLong("study_group_id"));
                invitation.setRecipientId(rs.getLong("user_id"));
                invitation.setStatus(Invitation.InvitationStatus.valueOf(rs.getString("status")));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    invitation.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    invitation.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                return invitation;
            }
            
            return null;
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
        }
    }
    
    private InvitationDTO getInvitationDTO(Connection conn, Invitation invitation) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            // Get user name
            String userSql = "SELECT first_name, last_name FROM Users WHERE user_id = ?";
            ps = conn.prepareStatement(userSql);
            ps.setLong(1, invitation.getRecipientId());
            rs = ps.executeQuery();
            
            String firstName = "";
            String lastName = "";
            if (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }
            
            // Create DTO
            InvitationDTO dto = new InvitationDTO();
            dto.setId(invitation.getId());
            dto.setGroupId(invitation.getGroupId());
            dto.setGroupName("Study Group " + invitation.getGroupId()); // Placeholder
            dto.setRecipientId(invitation.getRecipientId());
            dto.setRecipientName(firstName + " " + lastName);
            dto.setStatus(invitation.getStatus());
            
            return dto;
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
        }
    }

    /**
     * Check if a user is the admin for a specific group
     */
    public boolean isGroupAdmin(Long groupId, Long userId) {
        try (Connection conn = JDBCUtil.getConnection()) {
            // First check if the user is the creator of the group
            String sql = "SELECT COUNT(*) FROM StudyGroup " +
                        "WHERE study_group_id = ? AND admin_id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, groupId);
                stmt.setLong(2, userId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true;
                    }
                }
                
                // Also check StudyGroupMembers with ADMIN role
                String memberSql = "SELECT COUNT(*) FROM StudyGroupMembers " +
                                "WHERE study_group_id = ? AND user_id = ? AND group_role = 'ADMIN'";
                
                try (PreparedStatement memberStmt = conn.prepareStatement(memberSql)) {
                    memberStmt.setLong(1, groupId);
                    memberStmt.setLong(2, userId);
                    
                    try (ResultSet memberRs = memberStmt.executeQuery()) {
                        if (memberRs.next()) {
                            return memberRs.getInt(1) > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking admin status: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Check if a user is an AdminUser
     */
    public boolean isUserAdmin(Long userId) {
        try (Connection conn = JDBCUtil.getConnection()) {
            // Find all groups where this user is an admin
            String sql = "SELECT COUNT(*) FROM StudyGroup WHERE admin_id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // If the user is an admin of any group, consider them an admin user
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking admin status: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Process batch operations on invitations
     */
    public Map<String, Object> processBatchOperation(String operation, List<Long> invitationIds) {
        Map<String, Object> result = new HashMap<>();
        List<Long> processed = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        for (Long invitationId : invitationIds) {
            try {
                switch (operation) {
                    case "accept":
                        // Logic to accept invitation
                        processed.add(invitationId);
                        break;
                    case "decline":
                        // Logic to decline invitation
                        processed.add(invitationId);
                        break;
                    case "delete":
                        // Logic to delete invitation
                        processed.add(invitationId);
                        break;
                    default:
                        errors.add("Unknown operation: " + operation + " for invitation " + invitationId);
                }
            } catch (Exception e) {
                errors.add("Error processing invitation " + invitationId + ": " + e.getMessage());
            }
        }
        
        result.put("processed", processed);
        result.put("errors", errors);
        return result;
    }
}