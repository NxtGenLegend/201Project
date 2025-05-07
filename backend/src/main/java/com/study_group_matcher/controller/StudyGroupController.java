package com.study_group_matcher.controller;

import com.study_group_matcher.db.StudyGroupDBHelper;
import com.study_group_matcher.model.MeetingType;
import com.study_group_matcher.model.Privacy;
import com.study_group_matcher.model.StudyGroup;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/studygroup")
@CrossOrigin(origins = "*") // Optional: allow cross-origin requests from frontend
public class StudyGroupController {

    /*
     * I: JSON payload with adminID, groupName, course, meetingTime, meetingType,
     * location, privacy
     * P: Parse input and create a new StudyGroup, store in DB, return group ID
     * O: JSON response with status, message, and new groupID
     */
    @PostMapping("/create")
    public Map<String, Object> createStudyGroup(@RequestBody Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            int adminID = Integer.parseInt(data.get("adminID"));
            String groupName = data.get("groupName");
            String course = data.get("course");
            String location = data.get("location");
            LocalDateTime meetingTime = LocalDateTime.parse(data.get("meetingTime"));
            MeetingType meetingType = MeetingType.valueOf(data.get("meetingType").toUpperCase());
            Privacy privacy = Privacy.valueOf(data.get("privacy").toUpperCase());

            StudyGroup group = new StudyGroup(
                    -1, // auto-generated groupID
                    adminID,
                    groupName,
                    course,
                    meetingTime,
                    meetingType,
                    location,
                    privacy);

            int generatedGroupID = StudyGroupDBHelper.insertStudyGroup(group);

            if (generatedGroupID == -1) {
                throw new Exception("Failed to retrieve generated groupID.");
            }

            response.put("success", true);
            response.put("message", "Study group created successfully.");
            response.put("groupID", generatedGroupID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create study group: " + e.getMessage());
        }

        return response;
    }

    /*
     * I: groupID (path variable)
     * P: Retrieve specific study group by ID
     * O: JSON response with group data or error
     */
    @GetMapping("/{groupID}")
    public Map<String, Object> getStudyGroup(@PathVariable int groupID) {
        Map<String, Object> response = new HashMap<>();
        try {
            StudyGroup group = StudyGroupDBHelper.getStudyGroupByID(groupID);
            if (group != null) {
                response.put("success", true);
                response.put("studyGroup", group);
            } else {
                response.put("success", false);
                response.put("message", "Study group not found with ID: " + groupID);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving study group: " + e.getMessage());
        }
        return response;
    }

    /*
     * I: None
     * P: Retrieve all study groups
     * O: JSON response with list of groups
     */
    @GetMapping("/all")
    public Map<String, Object> getAllStudyGroups() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StudyGroup> groups = StudyGroupDBHelper.getAllStudyGroups();
            response.put("success", true);
            response.put("studyGroups", groups);
            response.put("count", groups.size());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving study groups: " + e.getMessage());
        }
        return response;
    }

    /*
     * I: groupID (path variable)
     * P: Delete study group by ID
     * O: JSON response with status
     */
    @DeleteMapping("/{groupID}")
    public Map<String, Object> deleteStudyGroup(@PathVariable int groupID) {
        Map<String, Object> response = new HashMap<>();
        try {
            StudyGroup group = StudyGroupDBHelper.getStudyGroupByID(groupID);
            if (group == null) {
                response.put("success", false);
                response.put("message", "Study group not found with ID: " + groupID);
                return response;
            }

            StudyGroupDBHelper.deleteStudyGroup(groupID);
            response.put("success", true);
            response.put("message", "Study group deleted successfully.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting study group: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/user/{userID}/groups")
    public Map<String, Object> getStudyGroupsForUser(@PathVariable int userID) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StudyGroup> groups = StudyGroupDBHelper.getStudyGroupsForUser(userID);
            response.put("success", true);
            response.put("studyGroups", groups);
            response.put("count", groups.size());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving user's study groups: " + e.getMessage());
        }
        return response;
    }
}