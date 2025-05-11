package com.study_group_matcher.controller;

import com.study_group_matcher.model.Inbox;
import com.study_group_matcher.db.InboxDBHelper;
import com.study_group_matcher.db.JDBCUtil;
import com.study_group_matcher.model.InboxDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/inbox")
public class InboxController {
    private final InboxDBHelper inboxDBHelper = new InboxDBHelper();

    @PostMapping("/inbox")
    public ResponseEntity<Long> createInbox(@RequestBody Map<String, Object> payload) {
        try (Connection conn = JDBCUtil.getConnection()) {
            InboxDBHelper inboxDBHelper = new InboxDBHelper(conn);

            long userId = ((Long) payload.get("user_id")).longValue();
            long senderId = ((Long) payload.get("sender_id")).longValue();
            long messageId = ((Long) payload.get("message_id")).longValue();
            long invitationId = ((Long) payload.get("invitation_id")).longValue();

            Inbox newInbox = new Inbox(userId, senderId, messageId, invitationId);

            inboxDBHelper.addInbox(newInbox);

            return ResponseEntity.status(HttpStatus.CREATED).body(newInbox.getInboxID());

        } catch (ClassCastException e) {
            return ResponseEntity.badRequest().body(-1L);  
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(-1L); 
        }
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> getAll(@PathVariable String username) {
        List<InboxDTO> responseList = new ArrayList<>();
        List<InboxDTO> inboxEntries = inboxDBHelper.getAll(username);
    
        for (InboxDTO inbox : inboxEntries) {
            responseList.add(new InboxDTO(inbox.getMessageId(), inbox.getInvitationId(), inbox.getContent()));
        }
        
        if (responseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inbox is empty.");
        }
        
        return ResponseEntity.ok(responseList);
    }

}

