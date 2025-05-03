package com.study_group_matcher.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.study_group_matcher.model.GroupMessage;
import com.study_group_matcher.db.GroupMessageDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/group-messages")
public class GroupMessageController {

    @Autowired
    private DataSource dataSource;

    @PostMapping
    public String sendGroupMessage(@RequestBody GroupMessage message) {
        try (Connection connection = dataSource.getConnection()) {
            GroupMessageDAO dao = new GroupMessageDAO(connection);
            dao.saveGroupMessage(message);
            return "Group message sent.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error sending group message.";
        }
    }

    @GetMapping
    public List<GroupMessage> getGroupMessages(@RequestParam String groupId) {
        try (Connection connection = dataSource.getConnection()) {
            GroupMessageDAO dao = new GroupMessageDAO(connection);
            return dao.getMessagesForGroup(groupId);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}