package com.wardconnect.ccms.controller;

import com.wardconnect.ccms.dto.PublicStatsResponse;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling public unauthenticated endpoints (landing page stats and complaint tracking).
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ComplaintService complaintService;

    public PublicController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/stats")
    public ResponseEntity<PublicStatsResponse> getPublicStats() {
        return ResponseEntity.ok(complaintService.getPublicStats());
    }

    @GetMapping("/track/{trackingId}")
    public ResponseEntity<Complaint> trackComplaint(@PathVariable String trackingId) {
        Complaint complaint = complaintService.findComplaintByTrackingId(trackingId);
        return ResponseEntity.ok(complaint);
    }
}
