package com.wardconnect.ccms.dto;

/**
 * DTO for admin dashboard stats summary.
 */
public class AdminStatsResponse {

    private long total;
    private long pending;
    private long inProgress;
    private long resolved;

    public AdminStatsResponse() {}

    public AdminStatsResponse(long total, long pending, long inProgress, long resolved) {
        this.total = total;
        this.pending = pending;
        this.inProgress = inProgress;
        this.resolved = resolved;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPending() {
        return pending;
    }

    public void setPending(long pending) {
        this.pending = pending;
    }

    public long getInProgress() {
        return inProgress;
    }

    public void setInProgress(long inProgress) {
        this.inProgress = inProgress;
    }

    public long getResolved() {
        return resolved;
    }

    public void setResolved(long resolved) {
        this.resolved = resolved;
    }
}
