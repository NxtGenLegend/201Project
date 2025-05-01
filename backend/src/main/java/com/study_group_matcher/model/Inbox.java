package com.study_group_matcher.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Inbox {

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }

    public enum NotificationStatus {
        NOTIFIED, SEEN
    }

    private long inbox_id;
    private long user_id;
    private long sender_id;
    private Status status = Status.PENDING;
    private NotificationStatus notification = NotificationStatus.NOTIFIED;
    private String contents;
    private LocalDateTime timestamp;


    private List<String> notifications = new ArrayList<>();

    public Inbox() {
        this.timestamp = LocalDateTime.now();
    }

    public Inbox(long user_id, long sender_id, String contents) {
        this.user_id = user_id;
        this.sender_id = sender_id;
        this.contents = contents;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public long getInboxId() {
        return inbox_id;
    }

    public void setInboxId(long inbox_id) {
        this.inbox_id = inbox_id;
    }

    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    public long getSenderId() {
        return sender_id;
    }

    public void setSenderId(long sender_id) {
        this.sender_id = sender_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public NotificationStatus getNotification() {
        return notification;
    }

    public void setNotification(NotificationStatus notification) {
        this.notification = notification;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Notification List Methods (not stored in DB but can be used in-app)
    public List<String> getNotifications() {
        return notifications;
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public boolean hasNotication(String notification) {
        return notifications.contains(notification);
    }

    public void deleteNotification(String notification) {
        notifications.remove(notification);
    }

    public void accept() {
        this.status = Status.ACCEPTED;
        timestamp = LocalDateTime.now();
    }
    public void decline() {
        this.status = Status.DECLINED;
        timestamp = LocalDateTime.now();
    }

    public void messageIsRead() {
        this.notification = NotificationStatus.SEEN;
    }

    public boolean isMessageRead() {
        return this.notification == NotificationStatus.SEEN;
    }

    public boolean isPending() {
        return this.status == Status.PENDING;
    }
}
