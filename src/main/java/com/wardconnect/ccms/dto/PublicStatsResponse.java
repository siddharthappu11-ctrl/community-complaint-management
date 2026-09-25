package com.wardconnect.ccms.dto;

/**
 * DTO for landing page public stats.
 */
public class PublicStatsResponse {

    private long totalReceived;
    private long totalResolved;
    private long totalInProgress;

    public PublicStatsResponse() {}

    public PublicStatsResponse(long totalReceived, long totalResolved, long totalInProgress) {
        this.totalReceived = totalReceived;
        this.totalResolved = totalResolved;
        this.totalInProgress = totalInProgress;
    }

    public long getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(long totalReceived) {
        this.totalReceived = totalReceived;
    }

    public long getTotalResolved() {
        return totalResolved;
    }

    public void setTotalResolved(long totalResolved) {
        this.totalResolved = totalResolved;
    }

    public long getTotalInProgress() {
        return totalInProgress;
    }

    public void setTotalInProgress(long totalInProgress) {
        this.totalInProgress = totalInProgress;
    }
}
