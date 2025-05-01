package com.study_group_matcher.model;

import java.time.LocalDateTime;

public class Invitation {
    private Long id;
    private Long groupId; 
    private Long recipientId;
    private InvitationStatus status = InvitationStatus.PENDING;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    
    public Invitation() {
    }
    
    public Invitation(Long groupId, Long recipientId) {
        this.groupId = groupId;
        this.recipientId = recipientId;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public Long getRecipientId() {
        return recipientId;
    }
    
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
    
    public InvitationStatus getStatus() {
        return status;
    }
    
    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void decline() {
        this.status = InvitationStatus.DECLINED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum InvitationStatus {
        PENDING, ACCEPTED, DECLINED
    }
}