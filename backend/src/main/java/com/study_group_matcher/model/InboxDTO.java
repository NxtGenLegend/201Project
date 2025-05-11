package com.study_group_matcher.model;

public class InboxDTO {
    private Long messageId;
    private Long invitationId;
    private String conetnt; 

    // All-args constructor (from @AllArgsConstructor)
    public InboxDTO(Long messageId, Long invitationId, String conetnt) {
        this.messageId = messageId;
        this.invitationId = invitationId;
        this.conetnt = conetnt;
    }

    // Getters
    public Long getMessageId() {
        return messageId;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public String getContents() {
        return conetnt;
    }

    // Setters
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public void setContents(String conetnt) {
        this.conetnt = conetnt;
    }
}
