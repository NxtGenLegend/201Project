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
public class StudyGroupController {

    
    /*
    * I: JSON payload via HTTP POST with this stuff:
    * adminID, groupName, course, meetingTime, meetingType, location, privacy
    * P: Parse input data, construct a StudyGroup object,
    * insert the group into the database,
    * and retrieve its generated groupID
    * O: JSON response with "success" status, "message", and the generated "groupID" if successful
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

            StudyGroup group = new StudyGroup(-1, adminID, groupName, course, meetingTime, meetingType, location, privacy);

            int generatedGroupID = StudyGroupDBHelper.insertStudyGroup(group);

            if (generatedGroupID == -1) {
                throw new Exception("Failed to retrieve generated groupID.");
            }

            response.put("success", true);
            response.put("message", "Study group created successfully.");
            response.put("groupID", generatedGroupID);
        }
        catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create study group: " + e.getMessage());
        }

        return response;
    }

     /*
    * I: Study group ID as a path variable
    * P: Retrieve study group information from the database using StudyGroupDBHelper
    * O: JSON response with study group data or error message if not found
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
    * P: Retrieve all study groups from the database using StudyGroupDBHelper
    * O: JSON response with list of all study groups
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
    * I: Study group ID as a path variable
    * P: Delete the study group from the database using StudyGroupDBHelper
    * O: JSON response with success/failure status and message
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

}