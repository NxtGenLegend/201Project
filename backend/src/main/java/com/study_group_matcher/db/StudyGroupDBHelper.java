package com.study_group_matcher.db;

import com.study_group_matcher.model.StudyGroup;
import com.study_group_matcher.model.MeetingType;
import com.study_group_matcher.model.Privacy;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudyGroupDBHelper {

    /*
    * I: StudyGroup object with initialized fields except groupID
    * P: Connect to the database using JDBCUtil, insert a new row into the StudyGroup table 
    *    with adminID, privacy, max_members, and creation_date;
    * O: Returns the auto-generated groupID if successful; -1 if insertion failed
    */
    public static int insertStudyGroup(StudyGroup group) {
        String query = "INSERT INTO StudyGroup (admin, privacy, max_members, creation_date) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
    
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    
            ps.setInt(1, group.getAdminID());
            ps.setString(2, group.getPrivacy().toString());
            ps.setInt(3, group.getMembers().size());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating study group failed, no rows affected.");
            }
    
            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating study group failed, no ID obtained.");
            }
        }
        catch (SQLException e) {
            System.err.println("Error inserting StudyGroup: " + e.getMessage());
        }
        finally {
            JDBCUtil.close(generatedKeys);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }
        return -1;
    }    

    /*
    * I: Unique group ID
    * P: Connects to the database and queries for the row in the StudyGroup table with that ID
    *    Builds a new StudyGroup object using retrieved data and placeholder values for unqueried fields
    * O: Returns a StudyGroup object if found, or null if the groupID is a lie
    */
    public static StudyGroup getStudyGroupByID(int groupID) {
        String query = "SELECT * FROM StudyGroup WHERE study_group_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StudyGroup group = null;

        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, groupID);
            rs = ps.executeQuery();

            if (rs.next()) {
                int adminID = rs.getInt("admin");
                String privacy = rs.getString("privacy");
                int maxMembers = rs.getInt("max_members");

                group = new StudyGroup(
                    groupID,
                    adminID,
                    "PlaceholderName",
                    "PlaceholderCourse",
                    LocalDateTime.now(),
                    MeetingType.IN_PERSON,
                    "PlaceholderLocation",
                    Privacy.valueOf(privacy)
                );
            }
        }
        catch (SQLException e) {
            System.err.println("Error retrieving StudyGroup: " + e.getMessage());
        }
        finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }

        return group;
    }

   /*
    * I: None
    * P: Connect to the database and selects all rows from the StudyGroup table
    *    For each row, build a StudyGroup object using the retrieved data and placeholder values
    * O: List of all StudyGroup objects stored in the database
    */
    public static List<StudyGroup> getAllStudyGroups() {
        String query = "SELECT * FROM StudyGroup";
        List<StudyGroup> groups = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int groupID = rs.getInt("study_group_id");
                int adminID = rs.getInt("admin");
                String privacy = rs.getString("privacy");

                StudyGroup group = new StudyGroup(
                        groupID,
                        adminID,
                        "PlaceholderName",
                        "PlaceholderCourse",
                        LocalDateTime.now(),
                        MeetingType.IN_PERSON,
                        "PlaceholderLocation",
                        Privacy.valueOf(privacy)
                );
                groups.add(group);
            }

        }
        catch (SQLException e) {
            System.err.println("Error retrieving all StudyGroups: " + e.getMessage());
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(stmt);
            JDBCUtil.close(conn);
        }

        return groups;
    }

    /*
    * I: Unique group ID to delete
    * P: Connect to the database and delete the record from the StudyGroup table with that ID
    * O: Remove the row from the database if it exists
    */
    public static void deleteStudyGroup(int groupID) {
        String query = "DELETE FROM StudyGroup WHERE study_group_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, groupID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting StudyGroup: " + e.getMessage());
        } finally {
            JDBCUtil.close(stmt);
            JDBCUtil.close(conn);
        }
    }
}