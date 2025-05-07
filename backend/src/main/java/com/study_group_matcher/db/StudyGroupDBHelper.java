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
     * P: Insert a new row into the StudyGroup table
     * O: Returns the auto-generated groupID if successful; -1 if insertion failed
     */
    public static int insertStudyGroup(StudyGroup group) {
        String query = "INSERT INTO StudyGroup (admin_id, group_name, course, meeting_time, meeting_type, location, privacy, max_members, current_member_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;

        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, group.getAdminID());
            ps.setString(2, group.getGroupName());
            ps.setString(3, group.getCourse());
            ps.setTimestamp(4, Timestamp.valueOf(group.getMeetingTime()));
            ps.setString(5, group.getMeetingType().toString());
            ps.setString(6, group.getLocation());
            ps.setString(7, group.getPrivacy().toString());
            ps.setInt(8, 50); // default max_members
            ps.setInt(9, 1); // admin is initial member

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating study group failed, no rows affected.");
            }

            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating study group failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting StudyGroup: " + e.getMessage());
        } finally {
            JDBCUtil.close(generatedKeys);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }

        return -1;
    }

    /*
     * I: Unique group ID
     * P: Retrieve that StudyGroup from the database
     * O: Returns a StudyGroup object if found; otherwise null
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
                int adminID = rs.getInt("admin_id");
                String groupName = rs.getString("group_name");
                String course = rs.getString("course");
                LocalDateTime meetingTime = rs.getTimestamp("meeting_time").toLocalDateTime();
                MeetingType meetingType = MeetingType.valueOf(rs.getString("meeting_type"));
                String location = rs.getString("location");
                Privacy privacy = Privacy.valueOf(rs.getString("privacy"));

                group = new StudyGroup(
                        groupID,
                        adminID,
                        groupName,
                        course,
                        meetingTime,
                        meetingType,
                        location,
                        privacy);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving StudyGroup: " + e.getMessage());
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(ps);
            JDBCUtil.close(conn);
        }

        return group;
    }

    /*
     * I: None
     * P: Retrieve all StudyGroups
     * O: List of StudyGroup objects
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
                int adminID = rs.getInt("admin_id");
                String groupName = rs.getString("group_name");
                String course = rs.getString("course");
                LocalDateTime meetingTime = rs.getTimestamp("meeting_time").toLocalDateTime();
                MeetingType meetingType = MeetingType.valueOf(rs.getString("meeting_type"));
                String location = rs.getString("location");
                Privacy privacy = Privacy.valueOf(rs.getString("privacy"));

                StudyGroup group = new StudyGroup(
                        groupID,
                        adminID,
                        groupName,
                        course,
                        meetingTime,
                        meetingType,
                        location,
                        privacy);
                groups.add(group);
            }
        } catch (SQLException e) {
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
     * P: Delete StudyGroup from the database
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

    public static List<StudyGroup> getStudyGroupsForUser(int userID) {
        String query = """
                    SELECT sg.*, u.display_name AS group_lead
                    FROM StudyGroup sg
                    JOIN StudyGroupMembers sgm ON sg.study_group_id = sgm.study_group_id
                    JOIN Users u ON sg.admin_id = u.user_id
                    WHERE sgm.user_id = ?
                """;

        List<StudyGroup> groups = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int groupID = rs.getInt("study_group_id");
                int adminID = rs.getInt("admin_id");
                String groupName = rs.getString("group_name");
                String course = rs.getString("course");
                LocalDateTime meetingTime = rs.getTimestamp("meeting_time").toLocalDateTime();
                MeetingType meetingType = MeetingType.valueOf(rs.getString("meeting_type"));
                String location = rs.getString("location");
                Privacy privacy = Privacy.valueOf(rs.getString("privacy"));

                StudyGroup group = new StudyGroup(
                        groupID,
                        adminID,
                        groupName,
                        course,
                        meetingTime,
                        meetingType,
                        location,
                        privacy);

                groups.add(group);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving StudyGroups for user: " + e.getMessage());
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(stmt);
            JDBCUtil.close(conn);
        }

        return groups;
    }

}