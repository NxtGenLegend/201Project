package com.study_group_matcher.model;

import java.time.LocalDateTime;  
public class Message {
    private int messageId;
    private int senderId;
    private int recipientId; // nullable for group messages
    private String messageBody;
    private LocalDateTime timestamp;

    // Constructor for inserting a new message
    public Message(int senderId, int recipientId, String messageBody) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageBody = messageBody;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for loading from database
    public Message(int messageId, int senderId, int recipientId, String messageBody, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessageContents() {
        return messageBody;
    }
}
