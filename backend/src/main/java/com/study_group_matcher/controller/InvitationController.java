package com.study_group_matcher.controller;

import com.study_group_matcher.model.AdminUser;
import com.study_group_matcher.model.InvitationDTO;
import com.study_group_matcher.model.InvitationService;
import com.study_group_matcher.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Get all pending invitations for a user
     */
    @GetMapping("/pending/{userId}")
    public ResponseEntity<?> getPendingInvitations(@PathVariable Long userId) {
        try {
            List<InvitationDTO> invitations = invitationService.getPendingInvitationsForUser(userId);
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to fetch invitations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new invitation - requires admin privileges
     */
    @PostMapping
    public ResponseEntity<?> createInvitation(
            @RequestBody Map<String, Object> request,
            @RequestParam Long creatorId) {
        try {
            // Extract request parameters
            Long groupId = Long.valueOf(request.get("groupId").toString());
            Long recipientId = Long.valueOf(request.get("recipientId").toString());
            
            if (groupId == null || recipientId == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Group ID and recipient ID are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if user is admin for this group
            boolean isAdmin = invitationService.isGroupAdmin(groupId, creatorId);
            if (!isAdmin) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Only group admins can send invitations");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
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
    public ResponseEntity<?> acceptInvitation(
            @PathVariable Long invitationId,
            @RequestParam Long userId) {
        try {
            InvitationDTO invitation = invitationService.acceptInvitation(invitationId, userId);
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
    public ResponseEntity<?> declineInvitation(
            @PathVariable Long invitationId,
            @RequestParam Long userId) {
        try {
            InvitationDTO invitation = invitationService.declineInvitation(invitationId, userId);
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to decline invitation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Administrative function to manage multiple invitations
     * Requires AdminUser privileges
     */
    @PostMapping("/admin/batch")
    public ResponseEntity<?> adminBatchOperation(
            @RequestBody Map<String, Object> request,
            @RequestParam Long adminUserId) {
        try {
            // Check if user is an admin
            boolean isAdmin = invitationService.isUserAdmin(adminUserId);
            if (!isAdmin) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "This operation requires administrative privileges");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            // Process batch operation
            String operation = (String) request.get("operation");
            List<Long> invitationIds = (List<Long>) request.get("invitationIds");
            
            Map<String, Object> result = invitationService.processBatchOperation(operation, invitationIds);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to process batch operation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}