package com.study_group_matcher.controller;

import com.study_group_matcher.model.InvitationDTO;
import com.study_group_matcher.model.InvitationService;
import com.study_group_matcher.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;
    
    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }
    
    /**
     * Get all pending invitations for the current user
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingInvitations(HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            List<InvitationDTO> invitations = invitationService.getPendingInvitationsForUser(currentUser.getUser_id());
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to fetch invitations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new invitation
     */
    @PostMapping
    public ResponseEntity<?> createInvitation(@RequestBody Map<String, Long> request, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            Long groupId = request.get("groupId");
            Long recipientId = request.get("recipientId");
            
            if (groupId == null || recipientId == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Group ID and recipient ID are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            InvitationDTO invitation = invitationService.createInvitation(groupId, recipientId);
            return new ResponseEntity<>(invitation, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to create invitation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Accept an invitation
     */
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            InvitationDTO invitation = invitationService.acceptInvitation(invitationId, currentUser.getUser_id());
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to accept invitation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Decline an invitation
     */
    @PostMapping("/{invitationId}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable Long invitationId, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            InvitationDTO invitation = invitationService.declineInvitation(invitationId, currentUser.getUser_id());
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to decline invitation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint to accept userId as a parameter 
     */
    @PostMapping("/{invitationId}/accept/{userId}")
    public ResponseEntity<?> acceptInvitationWithUserId(
            @PathVariable Long invitationId, 
            @PathVariable Long userId) {
        try {
            InvitationDTO invitation = invitationService.acceptInvitation(invitationId, userId);
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to accept invitation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}