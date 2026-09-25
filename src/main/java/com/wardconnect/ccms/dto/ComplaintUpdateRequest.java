package com.wardconnect.ccms.dto;

import com.wardconnect.ccms.enums.ComplaintStatus;

/**
 * DTO for updating complaint status and admin response.
 */
public class ComplaintUpdateRequest {

    private ComplaintStatus status;
    private String adminResponse;

    public ComplaintUpdateRequest() {}

    public ComplaintUpdateRequest(ComplaintStatus status, String adminResponse) {
        this.status = status;
        this.adminResponse = adminResponse;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }
}
