package com.study_group_matcher.model;

import java.sql.Timestamp;

public class InboxDTO {
    private Long messageId;
    private Long invitationId;
    private String sender;
    private String content;
    private Timestamp messageTime;
    private String groupName;
    private Timestamp invitationTime;
    
    public InboxDTO(Long messageId, Long invitationId, String content, String sender, Timestamp messageTime, String groupName, Timestamp invitationTime) {
        this.messageId = messageId;
        this.invitationId = invitationId;
        this.content = content;
        this.sender = sender; 
        this.messageTime = messageTime;
        this.groupName = groupName;
        this.invitationTime = invitationTime;
    }

    // Getters
    public Long getMessageId() {
        return messageId;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public Timestamp getInvitationTime() {
        return invitationTime;
    }

    // Setters
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setInvitationTime(Timestamp invitationTime) {
        this.invitationTime = invitationTime;
    }
}
