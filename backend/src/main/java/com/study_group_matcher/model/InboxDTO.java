package com.study_group_matcher.model;

public class InboxDTO {
    private Long messageId;
    private Long invitationId;
    private String content; 

    // All-args constructor (from @AllArgsConstructor)
    public InboxDTO(Long messageId, Long invitationId, String content) {
        this.messageId = messageId;
        this.invitationId = invitationId;
        this.content = content;
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
}
