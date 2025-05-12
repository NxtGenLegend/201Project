package com.study_group_matcher.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import com.study_group_matcher.model.Message;
import com.study_group_matcher.db.MessageDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // Optional: allow cross-origin requests from frontend
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private DataSource dataSource;

    @PostMapping("/dm")
    public String sendDirectMessage(@RequestBody Message message) {
        try (Connection connection = dataSource.getConnection()) {
            if (message.getTimestamp() == null) {
                message.setTimestamp(LocalDateTime.now());
            }
            MessageDao dao = new MessageDao(connection);
            dao.saveMessage(message);
            return "Message sent";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error saving message";
        }
    }

    @GetMapping("/dm")
    public List<Message> getConversation(@RequestParam int userA, @RequestParam int userB) {
        try (Connection connection = dataSource.getConnection()) {
            MessageDao dao = new MessageDao(connection);
            return dao.getConversation(userA, userB);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}