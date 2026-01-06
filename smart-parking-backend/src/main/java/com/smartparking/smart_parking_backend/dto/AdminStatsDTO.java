package com.smartparking.smart_parking_backend.dto;

public class AdminStatsDTO {
    private long totalUsers;
    private long totalOwners;
    private long totalBookings;
    private double totalRevenue;

    // Pending Verification
    private long pendingSlots;

    public AdminStatsDTO(long totalUsers, long totalOwners, long totalBookings, double totalRevenue,
            long pendingSlots) {
        this.totalUsers = totalUsers;
        this.totalOwners = totalOwners;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.pendingSlots = pendingSlots;
    }

    // Getters and Setters
    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(long totalOwners) {
        this.totalOwners = totalOwners;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getPendingSlots() {
        return pendingSlots;
    }

    public void setPendingSlots(long pendingSlots) {
        this.pendingSlots = pendingSlots;
    }
}
