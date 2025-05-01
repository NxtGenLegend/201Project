import java.sql.*;
import java.util.*;

public class MessageDao {
    private final Connection conn;

    public MessageDao(Connection conn) {
        this.conn = conn;
    }

    public void saveMessage(Message msg) throws SQLException {
        String sql = "INSERT INTO message (sender_id, recipient_id, message_body, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, msg.getSenderId());
            stmt.setInt(2, msg.getRecipientId());
            stmt.setString(3, msg.getMessageBody());
            stmt.setTimestamp(4, Timestamp.valueOf(msg.getTimestamp()));
            stmt.executeUpdate();
        }
    }

    public List<Message> getConversation(int userA, int userB) throws SQLException {
        String sql = """
            SELECT * FROM message
            WHERE (sender_id = ? AND recipient_id = ?) OR (sender_id = ? AND recipient_id = ?)
            ORDER BY timestamp
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userA);
            stmt.setInt(2, userB);
            stmt.setInt(3, userB);
            stmt.setInt(4, userA);

            ResultSet rs = stmt.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("sender_id"),
                    rs.getInt("recipient_id"),
                    rs.getString("message_body"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
            return messages;
        }
    }
}

