import java.time.LocalDateTime;

public class GroupMessage {
    private int messageId;
    private int senderId;
    private String groupId;
    private String messageBody;
    private LocalDateTime timestamp;

    // Constructor for new messages
    public GroupMessage(int senderId, String groupId, String messageBody) {
        this.senderId = senderId;
        this.groupId = groupId;
        this.messageBody = messageBody;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for messages loaded from the DB
    public GroupMessage(int messageId, int senderId, String groupId, String messageBody, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.groupId = groupId;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
    }

    // Getters
    public int getMessageId() {
        return messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
