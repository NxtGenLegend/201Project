package com.study_group_matcher.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String recipient; // can be a user ID or a group ID
    private LocalDateTime timestamp;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ChatMessage(MessageType type, String content, String sender, String recipient) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}