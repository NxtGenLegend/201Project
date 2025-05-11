package com.study_group_matcher.service;

import com.study_group_matcher.model.ChatMessage;
import com.study_group_matcher.model.Message;
import com.study_group_matcher.model.GroupMessage;
import com.study_group_matcher.db.MessageDao;
import com.study_group_matcher.db.GroupMessageDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private DataSource dataSource;

    public void savePrivateMessage(ChatMessage chatMessage) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            MessageDao messageDao = new MessageDao(connection);
            
            Message message = new Message(
                Integer.parseInt(chatMessage.getSender()),
                Integer.parseInt(chatMessage.getRecipient()),
                chatMessage.getContent()
            );
            
            messageDao.saveMessage(message);
        }
    }

    public void saveGroupMessage(ChatMessage chatMessage) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            GroupMessageDAO groupMessageDao = new GroupMessageDAO(connection);
            
            GroupMessage message = new GroupMessage(
                Integer.parseInt(chatMessage.getSender()),
                chatMessage.getRecipient(),
                chatMessage.getContent()
            );
            
            groupMessageDao.saveGroupMessage(message);
        }
    }

    public List<Message> getPrivateConversation(int userA, int userB) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            MessageDao messageDao = new MessageDao(connection);
            return messageDao.getConversation(userA, userB);
        }
    }

    public List<GroupMessage> getGroupMessages(String groupId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            GroupMessageDAO groupMessageDao = new GroupMessageDAO(connection);
            return groupMessageDao.getMessagesForGroup(groupId);
        }
    }
}