package com.study_group_matcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.study_group_matcher.model.ChatMessage;
import com.study_group_matcher.service.ChatService;

import java.sql.SQLException;
import java.util.Map;

@Controller
public class WebSocketChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendGroupMessage/{groupId}")
    @SendTo("/topic/group/{groupId}")
    public ChatMessage sendGroupMessage(@DestinationVariable String groupId, 
                                       @Payload ChatMessage chatMessage) {
        try {
            chatMessage.setRecipient(groupId);
            chatService.saveGroupMessage(chatMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatMessage;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public ChatMessage sendPrivateMessage(@Payload ChatMessage chatMessage) {
        try {
            chatService.savePrivateMessage(chatMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),
                "/queue/private",
                chatMessage
        );
        
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("username", chatMessage.getSender());
        }
        return chatMessage;
    }
}