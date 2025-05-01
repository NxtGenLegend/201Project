package com.study_group_matcher.controller;

import com.study_group_matcher.db.StudyGroupDBHelper;
import com.study_group_matcher.model.MeetingType;
import com.study_group_matcher.model.Privacy;
import com.study_group_matcher.model.StudyGroup;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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
            // Parse incoming fields except groupID
            int adminID = Integer.parseInt(data.get("adminID"));
            String groupName = data.get("groupName");
            String course = data.get("course");
            String location = data.get("location");

            LocalDateTime meetingTime = LocalDateTime.parse(data.get("meetingTime"));
            MeetingType meetingType = MeetingType.valueOf(data.get("meetingType").toUpperCase());
            Privacy privacy = Privacy.valueOf(data.get("privacy").toUpperCase());

            // Just temporarily use some dummy groupID 
            StudyGroup group = new StudyGroup(-1, adminID, groupName, course, meetingTime, meetingType, location, privacy);

            // Save and get generated the actual groupID
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
}