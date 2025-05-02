package com.study_group_matcher.model;

public class Inbox {
    private long inbox_id;
    private long user_id;
    private long sender_id;
    private long message_id;
    private long invitation_id;

    public Inbox() {}

    public Inbox(long user_id, long sender_id, long message_id, long invitation_id) {
        this.user_id = user_id;
        this.sender_id = sender_id;
        this.message_id = message_id;
        this.invitation_id = invitation_id;
    }

    // Getters
    public long getInboxID() {
        return inbox_id;
    }

    public long getUserID() {
        return user_id;
    }

    public long getSenderID() {
        return sender_id;
    }

    public long getMessageID() {
        return message_id;
    }

    public long getInvitationID() {
        return invitation_id;
    }

    // Setters
    public void setInboxID(long inbox_id) {
        this.inbox_id = inbox_id;
    }

    public void setUserID(long user_id) {
        this.user_id = user_id;
    }

    public void setSenderID(long sender_id) {
        this.sender_id = sender_id;
    }

    public void setMessageID(long message_id) {
        this.message_id = message_id;
    }

    public void setInvitationID(long invitation_id) {
        this.invitation_id = invitation_id;
    }
    
}
