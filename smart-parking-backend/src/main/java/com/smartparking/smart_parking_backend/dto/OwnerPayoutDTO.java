package com.smartparking.smart_parking_backend.dto;

public class OwnerPayoutDTO {
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String upiId;

    private double totalEarned;
    private double paidAmount;
    private double pendingAmount;

    public OwnerPayoutDTO(Long ownerId, String ownerName, String ownerEmail, String upiId, double totalEarned,
            double paidAmount) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.upiId = upiId;
        this.totalEarned = totalEarned;
        this.paidAmount = paidAmount;
        this.pendingAmount = totalEarned - paidAmount;
    }

    // Getters
    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getUpiId() {
        return upiId;
    }

    public double getTotalEarned() {
        return totalEarned;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }
}
