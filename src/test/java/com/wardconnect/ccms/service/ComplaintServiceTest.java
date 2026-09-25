package com.wardconnect.ccms.service;

import com.wardconnect.ccms.dto.AdminStatsResponse;
import com.wardconnect.ccms.dto.ComplaintCreateRequest;
import com.wardconnect.ccms.dto.ComplaintUpdateRequest;
import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.enums.Priority;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.model.Resident;
import com.wardconnect.ccms.repository.ComplaintRepository;
import com.wardconnect.ccms.service.impl.ComplaintServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintServiceImpl complaintService;

    private Resident resident;
    private Complaint complaint1;
    private Complaint complaint2;

    @BeforeEach
    void setUp() {
        resident = new Resident("user-1", "Maya Rao", "maya@example.com", "pass");
        complaint1 = new Complaint("c1", "CMP-4821", "Streetlight out", Category.STREETLIGHTS, "Lakeview Park",
                Priority.HIGH, "Broken light", null, ComplaintStatus.PENDING, "", "user-1", "Maya Rao");
        complaint2 = new Complaint("c2", "CMP-4818", "Garbage overflow", Category.GARBAGE, "Market Road",
                Priority.MEDIUM, "Bin full", null, ComplaintStatus.RESOLVED, "Cleaned", "user-1", "Maya Rao");
    }

    @Test
    void testCreateComplaint_Success() {
        ComplaintCreateRequest request = new ComplaintCreateRequest("Streetlight out", Category.STREETLIGHTS,
                "Lakeview Park", Priority.HIGH, "Broken light");

        when(complaintRepository.save(any(Complaint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Complaint result = complaintService.createComplaint(request, resident);

        assertNotNull(result);
        assertEquals("Streetlight out", result.getTitle());
        assertEquals(ComplaintStatus.PENDING, result.getStatus());
        assertEquals("user-1", result.getResidentId());
        assertTrue(result.getTrackingId().startsWith("CMP-"));
        verify(complaintRepository, times(1)).save(any(Complaint.class));
    }

    @Test
    void testFindComplaintsByResident() {
        when(complaintRepository.findByResidentIdOrderByCreatedAtDesc("user-1"))
                .thenReturn(Arrays.asList(complaint1, complaint2));

        List<Complaint> list = complaintService.findComplaintsByResident("user-1");

        assertEquals(2, list.size());
        assertEquals("Streetlight out", list.get(0).getTitle());
    }

    @Test
    void testUpdateComplaint_StatusAndResponse() {
        when(complaintRepository.findById("c1")).thenReturn(Optional.of(complaint1));
        when(complaintRepository.save(any(Complaint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ComplaintUpdateRequest updateReq = new ComplaintUpdateRequest(ComplaintStatus.IN_PROGRESS, "Inspector assigned");
        Complaint updated = complaintService.updateComplaint("c1", updateReq);

        assertEquals(ComplaintStatus.IN_PROGRESS, updated.getStatus());
        assertEquals("Inspector assigned", updated.getAdminResponse());
    }

    @Test
    void testGetAdminStats_UsingStreams() {
        when(complaintRepository.findAll()).thenReturn(Arrays.asList(complaint1, complaint2));

        AdminStatsResponse stats = complaintService.getAdminStats();

        assertEquals(2, stats.getTotal());
        assertEquals(1, stats.getPending());
        assertEquals(1, stats.getResolved());
        assertEquals(0, stats.getInProgress());
    }
}
