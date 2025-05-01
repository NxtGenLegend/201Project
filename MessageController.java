import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private Connection connection;  // Injected DB connection (or use a service)

    @PostMapping("/dm")
    public String sendDirectMessage(@RequestBody Message message) {
        MessageDao dao = new MessageDao(connection);
        try {
            dao.saveMessage(message);
            return "Message sent";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error saving message";
        }
    }

    @GetMapping("/dm")
    public List<Message> getConversation(@RequestParam int userA, @RequestParam int userB) {
        MessageDao dao = new MessageDao(connection);
        try {
            return dao.getConversation(userA, userB);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
}