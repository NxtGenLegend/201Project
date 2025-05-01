package com.study_group_matcher.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    
    @GetMapping("/pending")
    public ResponseEntity<List<InvitationDTO>> getPendingInvitations() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(invitationService.getPendingInvitationsForUser(userId));
    }
    
    @PostMapping
    public ResponseEntity<InvitationDTO> createInvitation(@RequestBody Map<String, Long> request) {
        Long groupId = request.get("groupId");
        Long recipientId = request.get("recipientId");
        
        if (groupId == null || recipientId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return new ResponseEntity<>(
                invitationService.createInvitation(groupId, recipientId),
                HttpStatus.CREATED
        );
    }
    
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<InvitationDTO> acceptInvitation(@PathVariable Long invitationId) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(invitationService.acceptInvitation(invitationId, userId));
    }
    
    @PostMapping("/{invitationId}/decline")
    public ResponseEntity<InvitationDTO> declineInvitation(@PathVariable Long invitationId) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(invitationService.declineInvitation(invitationId, userId));
    }
    
    // Helper method to get current user ID from the security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getUserId();
    }
}