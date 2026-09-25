package com.wardconnect.ccms.service;

import com.wardconnect.ccms.dto.*;
import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.model.User;

import java.util.List;

/**
 * OOP Concept: Interface & Abstraction & Polymorphism (Method Overloading)
 * Interface declaring complaint management operations.
 */
public interface ComplaintService {

    Complaint createComplaint(ComplaintCreateRequest request, User resident);

    Complaint findComplaintById(String id);

    Complaint findComplaintByTrackingId(String trackingId);

    List<Complaint> findComplaintsByResident(String residentId);

    // OOP Concept: Method Overloading
    List<Complaint> findComplaints();

    // OOP Concept: Method Overloading
    List<Complaint> findComplaints(ComplaintStatus status, Category category, String searchQuery);

    Complaint updateComplaint(String id, ComplaintUpdateRequest request);

    PublicStatsResponse getPublicStats();

    AdminStatsResponse getAdminStats();
}
