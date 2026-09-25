package com.wardconnect.ccms.controller;

import com.wardconnect.ccms.dto.AdminStatsResponse;
import com.wardconnect.ccms.dto.ComplaintUpdateRequest;
import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling admin operation console endpoints.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ComplaintService complaintService;

    public AdminController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q) {

        ComplaintStatus statusEnum = null;
        if (status != null && !status.equalsIgnoreCase("All") && !status.isBlank()) {
            try {
                statusEnum = ComplaintStatus.fromValue(status);
            } catch (Exception ignored) {}
        }

        Category categoryEnum = null;
        if (category != null && !category.equalsIgnoreCase("All") && !category.isBlank()) {
            try {
                categoryEnum = Category.fromValue(category);
            } catch (Exception ignored) {}
        }

        List<Complaint> complaints = complaintService.findComplaints(statusEnum, categoryEnum, q);
        return ResponseEntity.ok(complaints);
    }

    @PutMapping("/complaints/{id}")
    public ResponseEntity<Complaint> updateComplaint(
            @PathVariable String id,
            @RequestBody ComplaintUpdateRequest request) {

        Complaint updated = complaintService.updateComplaint(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getAdminStats() {
        return ResponseEntity.ok(complaintService.getAdminStats());
    }
}
