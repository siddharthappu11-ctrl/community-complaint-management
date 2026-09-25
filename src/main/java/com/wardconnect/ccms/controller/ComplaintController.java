package com.wardconnect.ccms.controller;

import com.wardconnect.ccms.dto.ComplaintCreateRequest;
import com.wardconnect.ccms.exception.UnauthorizedException;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.model.User;
import com.wardconnect.ccms.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling resident complaint management endpoints.
 */
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@Valid @RequestBody ComplaintCreateRequest request,
                                                     Authentication authentication) {
        User user = getUserFromAuth(authentication);
        Complaint created = complaintService.createComplaint(request, user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints(Authentication authentication) {
        User user = getUserFromAuth(authentication);
        List<Complaint> myComplaints = complaintService.findComplaintsByResident(user.getId());
        return ResponseEntity.ok(myComplaints);
    }

    private User getUserFromAuth(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Authentication token is missing or invalid.");
        }
        return (User) authentication.getPrincipal();
    }
}
