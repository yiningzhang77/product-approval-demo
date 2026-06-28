package com.example.productapproval.dto;

public class DashboardStats {

    private final long totalCount;
    private final long pendingCount;
    private final long approvedCount;
    private final long rejectedCount;
    private final long warningCount;

    public DashboardStats(long totalCount, long pendingCount, long approvedCount, long rejectedCount, long warningCount) {
        this.totalCount = totalCount;
        this.pendingCount = pendingCount;
        this.approvedCount = approvedCount;
        this.rejectedCount = rejectedCount;
        this.warningCount = warningCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public long getWarningCount() {
        return warningCount;
    }
}
