import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupMessageDAO {
    private final Connection conn;

    public GroupMessageDAO(Connection conn) {
        this.conn = conn;
    }

    public void saveGroupMessage(GroupMessage msg) throws SQLException {
        String sql = "INSERT INTO group_message (sender_id, group_id, message_body, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, msg.getSenderId());
            stmt.setString(2, msg.getGroupId());
            stmt.setString(3, msg.getMessageBody());
            stmt.setTimestamp(4, Timestamp.valueOf(msg.getTimestamp()));
            stmt.executeUpdate();
        }
    }

    public List<GroupMessage> getMessagesForGroup(String groupId) throws SQLException {
        String sql = "SELECT * FROM group_message WHERE group_id = ? ORDER BY timestamp";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, groupId);
            ResultSet rs = stmt.executeQuery();
            List<GroupMessage> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(new GroupMessage(
                    rs.getInt("message_id"),
                    rs.getInt("sender_id"),
                    rs.getString("group_id"),
                    rs.getString("message_body"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
            return messages;
        }
    }
}
