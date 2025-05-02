package com.study_group_matcher.model;

public class InboxDTO {
    private Long messageId;
    private Long invitationId;

    public InboxDTO(Long messageId, Long invitationId) {
        this.messageId = messageId;
        this.invitationId = invitationId;
    }
    
    public Long getMessageId() {
        return messageId;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }
}
