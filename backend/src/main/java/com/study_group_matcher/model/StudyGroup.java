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
        this.groupName = name;
        this.course = courseName;
        this.adminID = adminUser.getUserId();
        this.meetingTime = LocalDateTime.now();
        this.meetingType = MeetingType.IN_PERSON;
        this.location = "";
        this.privacy = Privacy.PUBLIC;
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
        Invitation invite = new Invitation((long) this.groupID, (long) user.getUserId());
        pendingInvites.add(invite);
    }

    // removes the user’s invitation from the pending queue then add them to the study group’s member list
    public void approveRequest(User user) {
        pendingInvites.removeIf(invite -> invite.getRecipientId().equals(user));
        members.add(user);
    }

    public void deleteGroup() {
        members.clear();
        messages.clear();
        pendingInvites.clear();
    }

    public int getGroupID() {
        return groupID;
    }
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
    public int getAdminID() {
        return adminID;
    }
    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public LocalDateTime getMeetingTime() {
        return meetingTime;
    }
    public void setMeetingTime(LocalDateTime meetingTime) {
        this.meetingTime = meetingTime;
    }
    public MeetingType getMeetingType() {
        return meetingType;
    }
    public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Privacy getPrivacy() {
        return privacy;
    }
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }
    public HashSet<User> getMembers() {
        return members;
    }
}