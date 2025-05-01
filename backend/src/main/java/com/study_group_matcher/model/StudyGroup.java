package com.study_group_matcher.model;

import java.time.LocalDateTime;
import java.util.*;

import lombok.Getter;
import lombok.Setter;

// these annotations will generate the getters and setters for all class fields at compile-time
@Getter
@Setter
public class StudyGroup {
    private int groupID;
    private int adminID;
    private String groupName;
    private String course;
    private LocalDateTime meetingTime;
     // Enum: IN_PERSON, VIRTUAL
    private MeetingType meetingType;
    private String location;
     // Enum: PUBLIC, PRIVATE
    private Privacy privacy;
    private HashSet<User> members = new HashSet<>();
    private List<Message> messages = new ArrayList<>();
    private Queue<Invitation> pendingInvites = new LinkedList<>();

    public StudyGroup(int groupID, int adminID, String groupName, String course,
                      LocalDateTime meetingTime, MeetingType meetingType,
                      String location, Privacy privacy) {
        this.groupID = groupID;
        this.adminID = adminID;
        this.groupName = groupName;
        this.course = course;
        this.meetingTime = meetingTime;
        this.meetingType = meetingType;
        this.location = location;
        this.privacy = privacy;
    }

    public StudyGroup(String name, String courseName, AdminUser adminUser) {
        //TODO Auto-generated constructor stub
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public void updateDetails(String groupName, String course, LocalDateTime meetingTime,
                              MeetingType meetingType, String location, Privacy privacy) {
        this.groupName = groupName;
        this.course = course;
        this.meetingTime = meetingTime;
        this.meetingType = meetingType;
        this.location = location;
        this.privacy = privacy;
    }

    public void sendInvite(User user) {
        Invitation invite = new Invitation(this, user);
        pendingInvites.add(invite);
    }

    // removes the user’s invitation from the pending queue then add them to the study group’s member list
    public void approveRequest(User user) {
        pendingInvites.removeIf(invite -> invite.getRecipient().equals(user));
        members.add(user);
    }

    public void deleteGroup() {
        members.clear();
        messages.clear();
        pendingInvites.clear();
    }
}
