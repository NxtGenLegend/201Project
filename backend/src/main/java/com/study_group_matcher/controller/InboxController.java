package com.study_group_matcher.controller;

import com.study_group_matcher.model.Inbox;
import com.study_group_matcher.study_group_matcher.InboxDao;
import com.study_group_matcher.db.InboxDBHelper;
import com.study_group_matcher.util.JDBCUtil;
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

    // Get all the message or invitation id that pertains to a certain user
    @GetMapping("/{user_id}")
    public ResponseEntity<InboxDTO> getAll(@PathVariable long user_id) {
        List<Inbox> inboxEntries = inboxDBHelper.getAll(user_id);
        List<InboxDTO> responseList = new ArrayList<>();

        for (Inbox inbox : inboxEntries) {
            Long message_id = inbox.getMessageID() != 0 ? inbox.getMessageID() : null;           // set the id if not 0 or set it to null
            Long invitation_id = inbox.getInvitationID() != 0 ? inbox.getInvitationID() : null;  // set the id if not 0 or set it to null 
            responseList.add(new InboxDTO(message_id, invitation_id));
        }

        if (responseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages or invitations found for this user.");
        }
        return ResponseEntity.ok(responseList);
    }
}

