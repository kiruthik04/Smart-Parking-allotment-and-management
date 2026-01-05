package com.smartparking.smart_parking_backend.dto;

public class OwnerDashboardSummary {

    private int totalSlots;
    private int totalCarCapacity;
    private int totalBikeCapacity;
    private int totalTruckCapacity;
    private long activeBookings;
    private double totalRevenue;

    public OwnerDashboardSummary(int totalSlots, int totalCarCapacity, int totalBikeCapacity, int totalTruckCapacity,
            long activeBookings, double totalRevenue) {
        this.totalSlots = totalSlots;
        this.totalCarCapacity = totalCarCapacity;
        this.totalBikeCapacity = totalBikeCapacity;
        this.totalTruckCapacity = totalTruckCapacity;
        this.activeBookings = activeBookings;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getTotalCarCapacity() {
        return totalCarCapacity;
    }

    public int getTotalBikeCapacity() {
        return totalBikeCapacity;
    }

    public int getTotalTruckCapacity() {
        return totalTruckCapacity;
    }

    public long getActiveBookings() {
        return activeBookings;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
