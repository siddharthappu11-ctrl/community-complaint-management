package com.wardconnect.ccms.service.impl;

import com.wardconnect.ccms.dto.*;
import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.exception.ResourceNotFoundException;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.model.User;
import com.wardconnect.ccms.repository.ComplaintRepository;
import com.wardconnect.ccms.service.ComplaintService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * OOP Concept: Interface Implementation, Polymorphism & Collections/Streams
 * Implementation of ComplaintService. Uses Java Streams for analytics.
 */
@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final Random random = new Random();

    public ComplaintServiceImpl(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Override
    public Complaint createComplaint(ComplaintCreateRequest request, User resident) {
        String trackingId = generateTrackingId();
        
        Complaint complaint = new Complaint(
                null,
                trackingId,
                request.getTitle().trim(),
                request.getCategory(),
                request.getLocation().trim(),
                request.getPriority(),
                request.getDescription().trim(),
                request.getPhotoPath(),
                ComplaintStatus.PENDING,
                "",
                resident.getId(),
                resident.getName()
        );

        return complaintRepository.save(complaint);
    }

    @Override
    public Complaint findComplaintById(String id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID: " + id));
    }

    @Override
    public Complaint findComplaintByTrackingId(String trackingId) {
        return complaintRepository.findByTrackingId(trackingId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with tracking ID: " + trackingId));
    }

    @Override
    public List<Complaint> findComplaintsByResident(String residentId) {
        return complaintRepository.findByResidentIdOrderByCreatedAtDesc(residentId);
    }

    // OOP Concept: Method Overloading (Find all complaints)
    @Override
    public List<Complaint> findComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc();
    }

    // OOP Concept: Method Overloading & Collections/Streams (Filtered complaints search)
    @Override
    public List<Complaint> findComplaints(ComplaintStatus status, Category category, String searchQuery) {
        List<Complaint> allComplaints = complaintRepository.findAllByOrderByCreatedAtDesc();
        
        // Using OOP Collections & Java Streams pipeline for in-memory filtering and searching
        return allComplaints.stream()
                .filter(c -> status == null || c.getStatus() == status)
                .filter(c -> category == null || c.getCategory() == category)
                .filter(c -> {
                    if (searchQuery == null || searchQuery.isBlank()) return true;
                    String q = searchQuery.toLowerCase().trim();
                    return c.getTitle().toLowerCase().contains(q) ||
                           c.getTrackingId().toLowerCase().contains(q) ||
                           c.getLocation().toLowerCase().contains(q) ||
                           c.getCategory().name().toLowerCase().contains(q) ||
                           c.getDescription().toLowerCase().contains(q);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Complaint updateComplaint(String id, ComplaintUpdateRequest request) {
        Complaint complaint = findComplaintById(id);

        if (request.getStatus() != null) {
            complaint.setStatus(request.getStatus());
        }
        if (request.getAdminResponse() != null) {
            complaint.setAdminResponse(request.getAdminResponse().trim());
        }
        complaint.setUpdatedAt(LocalDateTime.now());

        return complaintRepository.save(complaint);
    }

    // OOP Concept: Collections and Streams for statistics aggregation
    @Override
    public PublicStatsResponse getPublicStats() {
        List<Complaint> all = complaintRepository.findAll();

        long totalReceived = all.size();
        
        // Using Java Streams filter and count
        long totalResolved = all.stream()
                .filter(c -> c.getStatus() == ComplaintStatus.RESOLVED)
                .count();

        long totalInProgress = all.stream()
                .filter(c -> c.getStatus() == ComplaintStatus.IN_PROGRESS)
                .count();

        return new PublicStatsResponse(totalReceived, totalResolved, totalInProgress);
    }

    // OOP Concept: Collections and Streams (Grouping and counting by status)
    @Override
    public AdminStatsResponse getAdminStats() {
        List<Complaint> all = complaintRepository.findAll();

        Map<ComplaintStatus, Long> statusCounts = all.stream()
                .collect(Collectors.groupingBy(Complaint::getStatus, Collectors.counting()));

        long total = all.size();
        long pending = statusCounts.getOrDefault(ComplaintStatus.PENDING, 0L);
        long inProgress = statusCounts.getOrDefault(ComplaintStatus.IN_PROGRESS, 0L);
        long resolved = statusCounts.getOrDefault(ComplaintStatus.RESOLVED, 0L);

        return new AdminStatsResponse(total, pending, inProgress, resolved);
    }

    private String generateTrackingId() {
        int randomNum = 1000 + random.nextInt(9000);
        return "CMP-" + randomNum;
    }
}
